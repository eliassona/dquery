(ns dquery.core-test
  (:require [clojure.test :refer :all]
            [dquery.core :refer :all]))
(deftest test-query
  (defmethod entity-of :test/property1 [_] {:a 1, :b 2, :c {:x 10, :y 11}})
  (defmethod entity-of :test/property2 [_] {:ax 10, :bx 20, :cx {:xx 100, :yx 110}})
;  (is (= {:a 1, :b 2, :c {:x 10, :y 11}} (query :test/property1)))
  (is (= {:test/property1 {:a 1, :b 2, :c {:x 10, :y 11}}} (query [:test/property1])))
  (is (=  {:test/property1 {:a 1, :b 2, :c {:x 10, :y 11}}, 
           :test/property2 {:ax 10, :bx 20, :cx {:xx 100, :yx 110}}} 
          (query [:test/property1 :test/property2])))
  (is (= {:test/property1 {:a 1}} (query [{:test/property1 [:a]}])))
  (is (= {:test/property1 {:a 1}, :test/property2 {:bx 20}} (query [{:test/property1 [:a]} {:test/property2 [:bx]}])))
;  (is (= [{:test/property1 {}}] (query [{:test/property1 []}])))
;  (is (= [{:test/property1 {:a 1, :b 2, :c [:x 10, :y 11]}}] (query [{:test/property1 [*]}])))
  )


