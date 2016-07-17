(ns dquery.core)

(defmacro dbg [body]
  `(let [x# ~body]
     (println "dbg:" '~body "=" x#)
     x#))
(defmulti entity-of (fn [kw] kw))

(defprotocol IDQuery
  (query-impl [expr context]))


(defn seq-or-map [context expr]
  (if (map? context)
    {expr (context expr)}
    (mapv (fn [v] {expr (expr v)}) context)
    ))
  

(extend-protocol IDQuery
  clojure.lang.IPersistentVector
  (query-impl [expr context] (reduce merge (map (fn [e] (query-impl e context))  expr)))
  clojure.lang.IPersistentList
  (query-impl [expr context])
  clojure.lang.Keyword
  (query-impl [expr context] (if context (seq-or-map context expr) {expr (entity-of expr)}))
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


