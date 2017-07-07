(ns binary-serialization.primitive-test
  (:require [clojure.test :refer :all]
            [binary-serialization.codec :refer :all]
            [binary-serialization.primitive :refer :all]
            [clojure.spec.alpha :as s]))

(deftest primitive-size
  (testing "unsigned"
    (is (= 1 (sizeof uint8 0xae)))
    (is (= 2 (sizeof uint16 0xae)))
    (is (= 4 (sizeof uint32 0xae))))
  (testing "signed"
    (is (= 1 (sizeof int8 0xae)))
    (is (= 2 (sizeof int16 0xae)))
    (is (= 4 (sizeof int32 0xae)))
    (is (= 8 (sizeof int64 0xae)))))


