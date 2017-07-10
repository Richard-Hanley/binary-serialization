(ns binary-serialization.codec
  (:import java.nio.ByteBuffer)
  (:require [clojure.spec.alpha :as s]))

(defprotocol Codec
  (primitive* [this])
  (specification* [this])
  (sizeof* [this value])
  (to-buffer* [this value buffer])
  (from-buffer* [this buffer]))

(extend-protocol Codec
  nil
  (primitive* [_] true)
  (specification* [_] (s/conformer (fn [_] nil)))
  (sizeof* [_ _] 0)
  (to-buffer* [_ _ _] nil)
  (from-buffer* [_ _ _] nil))

(defn primitive? [codec] (primitive* codec))
(defn composite? [codec] (not (primitive? codec)))

(defn specification
  [codec] (specification* codec))

(defn sizeof
  [codec value] (sizeof* codec value))

(defn to-buffer!
  [codec value buffer] (to-buffer* codec value buffer))

(defn from-buffer!
  [codec buffer] (from-buffer* codec buffer))

(defrecord CodecSpec [additional-spec codec]
  Codec
  (primitive* [_] (primitive? codec))
  (specification* [_] (s/and additional-spec (specification codec)))
  (sizeof* [_ value] (sizeof codec))
  (to-buffer* [_ value buffer] (to-buffer! codec value buffer))
  ; Make sure the result from the inner codec conforms after reading from the buffer
  (from-buffer* [this buffer] (s/conform (specification this) (from-buffer! codec buffer))))


(defn specify 
  "Takes a codec, and a specification, and returns a new codec
  that applies the spec before processing the data"
  [codec additional-spec] (map->CodecSpec {:codec codec :additional-spec additional-spec}))

(defn constant 
  "Takes a codec, and a specific value, and returns a new
  codec that will only accept nil or the value, and conforms to the value"
  [codec value] (specify codec (s/conformer #(if (or (nil? %) (= value %))
                                               value
                                               ::s/invalid))))

(defn default
  "Takes a codec and a value.  Returns a new codec that accepts a value
  or nil.  If the value is nil, then the default value is returned"
  [codec value] (specify codec (s/conformer #(if (nil? %)
                                               value
                                               %))))


(defrecord AssciatedField [field codec])
