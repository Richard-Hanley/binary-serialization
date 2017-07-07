(ns binary-serialization.primitive
  (:import java.nio.ByteBuffer)
  (:require [clojure.spec.alpha :as s]
            [binary-serialization.codec :as codec])); Unsigned types  

(defmacro sizeof-number [NumberType]
  `(/ (. ~NumberType SIZE) 8))

(defmacro max-signed-number 
  "Adds one to the max value since clojure spec ranges are exlusive on the
  max value.  For example (s/int-in-range? 0 128) will return false when 256 is supplied.
  For that reason we want (max-signed-number Byte) to return 128, not 127"
  [NumberType]
  `(inc (. ~NumberType MAX_VALUE)))

(defmacro min-signed-number [NumberType]
  `(. ~NumberType MIN_VALUE))

(defmacro max-unsigned-number [NumberType]
  `(bit-shift-left 1 (. ~NumberType SIZE)))

; Does not include uint64 since that is not supported without BigInt
(def uint8 
  (reify codec/Codec
    (primitive* [_] true)
    (conform* [this value]
      (s/conform
        (s/and
          (s/int-in 0 (max-unsigned-number Byte))
          (s/conformer unchecked-byte))
        value))
    (sizeof* [this _] (sizeof-number Byte))
    (to-buffer* [this value buffer] (.put buffer value))
    (from-buffer* [this buffer] (.get buffer ))))

(def uint16 
  (reify codec/Codec
    (primitive* [_] true)
    (conform* [this value]
      (s/conform
        (s/and
          (s/int-in 0 (max-unsigned-number Short))
          (s/conformer unchecked-short))
        value))
    (sizeof* [this _] (sizeof-number Short))
    (to-buffer* [this value buffer] (.putShort buffer value))
    (from-buffer* [this buffer] (.getShort buffer ))))

(def uint32 
  (reify codec/Codec
    (primitive* [_] true)
    (conform* [this value]
      (s/conform
        (s/and
          (s/int-in 0 (max-unsigned-number Integer))
          (s/conformer unchecked-int))
        value))
    (sizeof* [this _] (sizeof-number Integer))
    (to-buffer* [this value buffer] (.putInt buffer value))
    (from-buffer* [this buffer] (.getInt buffer ))))

; ; Signed types
; ; Still uses unchecked conversions to stop exceptions from being thrown
(def int8 
  (reify codec/Codec
    (primitive* [_] true)
    (conform* [this value]
      (s/conform
        (s/and
          (s/int-in (min-signed-number Byte) (max-signed-number Byte))
          (s/conformer unchecked-byte))
        value))
    (sizeof* [this _] (sizeof-number Byte))
    (to-buffer* [this value buffer] (.put buffer value))
    (from-buffer* [this buffer] (.get buffer ))))

(def int16 
  (reify codec/Codec
    (primitive* [_] true)
    (conform* [this value]
      (s/conform
        (s/and
          (s/int-in (min-signed-number Short) (max-signed-number Short))
          (s/conformer unchecked-short))
        value))
    (sizeof* [this _] (sizeof-number Short))
    (to-buffer* [this value buffer] (.putShort buffer value))
    (from-buffer* [this buffer] (.getShort buffer ))))

(def int32 
  (reify codec/Codec
    (primitive* [_] true)
    (conform* [this value]
      (s/conform
        (s/and
          (s/int-in (min-signed-number Integer) (max-signed-number Integer))
          (s/conformer unchecked-int))
        value))
    (sizeof* [this _] (sizeof-number Integer))
    (to-buffer* [this value buffer] (.putInt buffer value))
    (from-buffer* [this buffer] (.getInt buffer ))))

(def int64
  (reify codec/Codec
    (primitive* [_] true)
    (conform* [this value] 
      (try (long value)
           (catch IllegalArgumentException _ ::s/invalid)))
    (sizeof* [this _] (sizeof-number Long))
    (to-buffer* [this value buffer] (.putLong buffer value))
    (from-buffer* [this buffer] (.getLong buffer ))))
