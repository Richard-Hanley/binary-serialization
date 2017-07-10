(ns binary-serialization.codec
  (:import java.nio.ByteBuffer)
  (:require [clojure.spec.alpha :as s]))

(defprotocol Codec
  (primitive* [this])
  (conform* [this value])
  (sizeof* [this value])
  (to-buffer* [this value buffer])
  (from-buffer* [this buffer]))

(extend-protocol Codec
  nil
  (primitive* [_] true)
  (conform* [_ _] nil)
  (sizeof* [_ _] 0)
  (to-buffer* [_ _ _] nil)
  (from-buffer* [_ _ _] nil))

(defn primitive? [codec] (primitive* codec))
(defn composite? [codec] (not (primitive? codec)))

(defn conform
  [codec value] (conform* codec value))

(defn sizeof
  [codec value] (sizeof* codec value))

(defn to-buffer!
  [codec value buffer] (to-buffer* codec value buffer))

(defn from-buffer!
  [codec buffer] (from-buffer* codec buffer))

(defrecord CodecSpec [spec codec])

(defn specify 
  "Takes a codec, and a specification, and returns a new codec
  that applies the spec before processing the data"
  [codec spec])

(defn constant 
  "Takes a codec, and a specific value, and returns a new
  codec that will only accept nil or the value, and conforms to the value"
  [codec value])

(defn default
  "Takes a codec and a value.  Returns a new codec that accepts a value
  or nil.  If the value is nil, then the default value is returned"
  [codec value])


(defrecord AssciatedField [field codec])
