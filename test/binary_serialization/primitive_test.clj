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
    (is (= -1 (s/conform (codec/specification uint8) 0xff)))
    (is (= Byte (type (s/conform (codec/specification uint8) 0xff))))
    (is (= -1 (s/conform (codec/specification uint16) 0xffff)))
    (is (= Short (type (s/conform (codec/specification uint16) 0xffff))))
    (is (= -1 (s/conform (codec/specification uint32) 0xffffffff)))
    (is (= Integer (type (s/conform (codec/specification uint32) 0xffffffff)))))
  (testing "signed"
    (is (= Byte/MAX_VALUE (s/conform (codec/specification int8) Byte/MAX_VALUE)))
    (is (= Byte (type (s/conform (codec/specification int8) Byte/MAX_VALUE))))
    (is (= Short/MAX_VALUE (s/conform (codec/specification int16) Short/MAX_VALUE)))
    (is (= Short (type (s/conform (codec/specification int16) Short/MAX_VALUE))))
    (is (= Integer/MAX_VALUE (s/conform (codec/specification int32) Integer/MAX_VALUE)))
    (is (= Integer (type (s/conform (codec/specification int32) Integer/MAX_VALUE)))))
    (is (= Long/MAX_VALUE (s/conform (codec/specification int64) Long/MAX_VALUE)))
    (is (= Long (type (s/conform (codec/specification int64) Long/MAX_VALUE)))))

(deftest zero
  (testing "unsigned"
    (is (= 0 (s/conform (codec/specification uint8) 0)))
    (is (= Byte (type (s/conform (codec/specification uint8) 0))))
    (is (= 0 (s/conform (codec/specification uint16) 0)))
    (is (= Short (type (s/conform (codec/specification uint16) 0))))
    (is (= 0 (s/conform (codec/specification uint32) 0)))
    (is (= Integer (type (s/conform (codec/specification uint32) 0)))))
  (testing "signed"
    (is (= 0 (s/conform (codec/specification int8) 0)))
    (is (= Byte (type (s/conform (codec/specification int8) 0))))
    (is (= 0 (s/conform (codec/specification int16) 0)))
    (is (= Short (type (s/conform (codec/specification int16) 0))))
    (is (= 0 (s/conform (codec/specification int32) 0)))
    (is (= Integer (type (s/conform (codec/specification int32) 0)))))
    (is (= 0 (s/conform (codec/specification int64) 0)))
    (is (= Long (type (s/conform (codec/specification int64) 0)))))

(deftest negative
  (testing "unsigned"
    (is (s/invalid? (s/conform (codec/specification uint8) Byte/MIN_VALUE)))
    (is (s/invalid? (s/conform (codec/specification uint16) Short/MIN_VALUE)))
    (is (s/invalid? (s/conform (codec/specification uint32) Integer/MIN_VALUE))))
  (testing "signed"
    (is (= Byte/MIN_VALUE (s/conform (codec/specification int8) Byte/MIN_VALUE)))
    (is (= Byte (type (s/conform (codec/specification int8) Byte/MIN_VALUE))))
    (is (= Short/MIN_VALUE (s/conform (codec/specification int16) Short/MIN_VALUE)))
    (is (= Short (type (s/conform (codec/specification int16) Short/MIN_VALUE))))
    (is (= Integer/MIN_VALUE (s/conform (codec/specification int32) Integer/MIN_VALUE)))
    (is (= Integer (type (s/conform (codec/specification int32) Integer/MIN_VALUE)))))
    (is (= Long/MIN_VALUE (s/conform (codec/specification int64) Long/MIN_VALUE)))
    (is (= Long (type (s/conform (codec/specification int64) Long/MIN_VALUE)))))

(deftest overflow
  (testing "unsigned"
    (is (s/invalid? (s/conform (codec/specification uint8) 0x100)))
    (is (s/invalid? (s/conform (codec/specification uint16) 0x10000)))
    (is (s/invalid? (s/conform (codec/specification uint32) 0x100000000))))
  (testing "unsigned"
    (is (s/invalid? (s/conform (codec/specification int8) 0x80)))
    (is (s/invalid? (s/conform (codec/specification int16) 0x8000)))
    (is (s/invalid? (s/conform (codec/specification int32) 0x80000000))))
    (is (s/invalid? (s/conform (codec/specification int64) 0x8000000000000000))))

(deftest underflow 
  (testing "unsigned"
    (is (s/invalid? (s/conform (codec/specification uint8) -1)))
    (is (s/invalid? (s/conform (codec/specification uint16) -1 )))
    (is (s/invalid? (s/conform (codec/specification uint32) -1)))))


