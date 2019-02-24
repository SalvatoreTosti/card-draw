(ns card-draw.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))


(defn card-land []
  {:type :land
   :name "forest"})

(defn card-creature []
  {:type :creature
   :name "zo zu"})

(defn card-spell []
  {:type :spell
  :name "sylvan might"})

(defn deck []
   (concat
     (repeatedly 20 card-creature)
     (repeatedly 20 card-land)
     (repeatedly 20 card-spell)))

(defn first-hand []
  (->> (deck)
       (shuffle)
       (take 7)))

(defn random-deck []
  (->> (deck)
       (shuffle)))

(->> (map #(nth-type % 8) (repeatedly 10000 random-deck))
     (frequencies)
     (frequencies-to-percentages)
     (map-values double))



(defn map-values [f m]
  (into {} (for [[k v] m] [k (f v)])))

(defn frequencies-to-percentages [freqs]
  (let [total-count (reduce + (map val freqs))]
    (map-values #(* 100 (/ % total-count)) freqs)))

(defn nth-type [deck n]
  (-> deck
      (nth  n)
      (:type)))


;; general questions
;; how often can I play on curve?
;; what is the average composition of my first hand
;; how often will I run out of land on my way to 10?
;; what does my deck land curve look like?
;; what does my spell v. creature curve look like?
;;


;; generate complete card draws
;; generate hand analysis
;; generate





;find percentage of draws where 8th card is land

