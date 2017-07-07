(ns binary-serialization.primitive-test
  (:require [clojure.test :refer :all]
            [binary-serialization.codec :as codec]
            [binary-serialization.primitive :refer :all]
            [clojure.spec.alpha :as s]))

(deftest primitive-size
  (testing "unsigned"
    (is (= 1 (codec/sizeof uint8 0xae)))
    (is (= 2 (codec/sizeof uint16 0xae)))
    (is (= 4 (codec/sizeof uint32 0xae))))
  (testing "signed"
    (is (= 1 (codec/sizeof int8 0xae)))
    (is (= 2 (codec/sizeof int16 0xae)))
    (is (= 4 (codec/sizeof int32 0xae)))
    (is (= 8 (codec/sizeof int64 0xae)))))

(deftest max-value
  (testing "unsigned"
    (is (= -1 (codec/conform uint8 0xff)))
    (is (= Byte (type (codec/conform uint8 0xff))))
    (is (= -1 (codec/conform uint16 0xffff)))
    (is (= Short (type (codec/conform uint16 0xffff))))
    (is (= -1 (codec/conform uint32 0xffffffff)))
    (is (= Integer (type (codec/conform uint32 0xffffffff)))))
  (testing "signed"
    (is (= Byte/MAX_VALUE (codec/conform int8 Byte/MAX_VALUE)))
    (is (= Byte (type (codec/conform int8 Byte/MAX_VALUE))))
    (is (= Short/MAX_VALUE (codec/conform int16 Short/MAX_VALUE)))
    (is (= Short (type (codec/conform int16 Short/MAX_VALUE))))
    (is (= Integer/MAX_VALUE (codec/conform int32 Integer/MAX_VALUE)))
    (is (= Integer (type (codec/conform int32 Integer/MAX_VALUE)))))
    (is (= Long/MAX_VALUE (codec/conform int64 Long/MAX_VALUE)))
    (is (= Long (type (codec/conform int64 Long/MAX_VALUE)))))
