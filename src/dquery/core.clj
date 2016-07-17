(ns dquery.core)

(defmulti entity-of (fn [kw] kw))

(defprotocol IDQuery
  (query-impl [expr context]))



(defmethod entity-of :test/property [_] {:a 1, :b 2, :c {:x 10, :y 11}})

(extend-protocol IDQuery
  clojure.lang.IPersistentVector
  (query-impl [expr context] (reduce merge (map (fn [e] (query-impl e context))  expr)))
  clojure.lang.IPersistentList
  (query-impl [expr context])
  clojure.lang.Keyword
  (query-impl [expr context] {expr (if context (context expr) (entity-of expr))})
  clojure.lang.IPersistentMap
  (query-impl [expr context] 
    (into {} 
      (mapv 
        (fn [e] 
          (let [k (key e)]
            [k
            (query-impl (val e) (k (query-impl k context)))])) expr)))
)


(defmethod entity-of :default [kw] (throw (IllegalArgumentException. (format "%s is not a valid keyword" kw))))
(defmethod entity-of :system/property [_] (into {} (mapv (fn [e] [(-> e key keyword) (val e)]) (System/getProperties))))

(defmacro query [expr] `(query-impl ~expr nil)) 


