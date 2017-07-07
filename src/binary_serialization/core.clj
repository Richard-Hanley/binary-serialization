(ns binary-serialization.core
  (:use [binary-serialization.codec]))

(defrecord Node [codec raw-data conformed-data])

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))
