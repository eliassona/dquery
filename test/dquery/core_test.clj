(ns dquery.core-test
  (:require [clojure.test :refer :all]
            [dquery.core :refer :all]))

;;define two entities
(defmethod entity-of :test/entity1 [_] {:a 1, :b 2, :c {:x 10, :y 11}})
(defmethod entity-of :test/entity2 [_] {:ax 10, :bx 20, :cx {:xx 100, :yx 110}, :d [{:x 20, :y 30}, {:x 30, :y 40}]})
(defmethod entity-of :test/entity3 [_] [{:x 20, :y 30}, {:x 30, :y 40}])


(deftest one-entity
  (is (= {:test/entity1 {:a 1, :b 2, :c {:x 10, :y 11}}} (query :test/entity1))))

(deftest one-entity-of-many
  (is (= {:test/entity1 {:a 1, :b 2, :c {:x 10, :y 11}}} 
         (query [:test/entity1]))))
  
(deftest two-entities
  (is (=  {:test/entity1 {:a 1, :b 2, :c {:x 10, :y 11}}, 
           :test/entity2 {:ax 10, :bx 20, :cx {:xx 100, :yx 110}, :d [{:x 20, :y 30}, {:x 30, :y 40}]}} 
          (query [:test/entity1 :test/entity2]))))
  
(deftest one-entity-one-joins
  (is (= {:test/entity1 {:a 1}} 
         (query [{:test/entity1 [:a]}]))))

(deftest one-entity-two-join
  (is (= {:test/entity1 {:c {:y 11}}} 
         (query [{:test/entity1 [{:c [:y]}]}]))))

(deftest one-entity-join-to-list
  (is (= {:test/entity2 {:d [{:x 20}, {:x 30}]}} 
         (query [{:test/entity2 [{:d [:x]}]}]))))

(deftest two-entities-two-joins
  (is (= {:test/entity1 {:a 1}, :test/entity2 {:bx 20}} 
         (query [{:test/entity1 [:a]} {:test/entity2 [:bx]}]))))

(deftest one-entity-zero-joins
  #_(is (= [{:test/entity1 {}}] 
          (query [{:test/entity1 []}]))))


(deftest one-list-entity
  (is (= {:test/entity3 [{:x 20, :y 30} {:x 30, :y 40}]} 
         (query :test/entity3))))

(deftest one-list-entity-one-join
  (is (= {:test/entity3 [{:x 20} {:x 30}]} 
         (query [{:test/entity3 [:x]}]))))

;(is (= [{:test/entity1 {:a 1, :b 2, :c [:x 10, :y 11]}}] (query [{:test/entity1 [*]}])))
  


