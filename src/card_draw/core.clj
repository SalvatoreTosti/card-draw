(ns card-draw.core
  (:gen-class)
  (:require
    [clojure.java.io :as io]
    [cheshire.core :as cheshire]))

(defn map-values [f m]
  (into {} (for [[k v] m] [k (f v)])))

(defn frequencies-to-percentages [freqs]
  (let [total-count (reduce + (map val freqs))]
    (map-values #(* 100 (/ % total-count)) freqs)))

(defn nth-type [deck n]
  (-> deck
      (nth  n)
      (:type)))

(defn load-card-db-raw []
  (cheshire/parsed-seq (io/reader (io/resource "AllCards.json")) true))

(def load-card-db
  (memoize load-card-db-raw))

(defn get-card [card-name]
  (let [db  (first (load-card-db))]
    (-> (filter #(= (keyword card-name) (first %)) db)
      (first)
    )))


(defn test-deck []
  {
    "Mountain" 2,
   "Island" 14
   "Izzet Guildgate" 2,
   "Swiftwater Cliffs" 4,
   "Invert" 1,
   "Invent" 1,
   "Nivix Cyclops" 1

   })


(defn fetch-deck [deck]
  (map #(get-card %) (keys deck)))

(defn split-card? [card]
  (-> card
      (val)
      (:layout)
      (= "split")))

(defn card-count-split-adjust [entry]
  (if (split-card? (get-card (key entry)))
      (/ (val entry) 2)
      (val entry)))

(defn deck-count [deck]
  (reduce + (map #(card-count-split-adjust %) deck)))

(defn generate-card-listings [entry]
  (->> entry
       (key)
       (get-card)
       (repeat)
       (take (val entry))))

(defn build-deck [deck-listing]
  (->> deck-listing
       (map generate-card-listings)
       (reduce concat)))


(defn relevant-stats [card]
  {
    :name (:name card)
    :types (:types card)
;;     :colors (:colors card)
;;     :colorIdentity (:colorIdentity card)
   }
  )

(defn random-draw-list []
(->> (build-deck (test-deck))
     (shuffle)
     (map second)
     (map relevant-stats)
     ))

(defn random-draw-indexed []
(->> (random-draw-list)
     (map :types)
     (flatten)
     (index-list-map)
     ))

(->>
  (take 100 (repeatedly #(random-draw-indexed)))
  (flatten)
  (frequencies))




(defn index-list-map [coll]
  (index-list-map-rec coll, [], 1))


(defn- index-list-map-rec [coll, x, ctr]
  (if
    (empty? coll) x
    (index-list-map-rec (rest coll) (conj x {ctr, (first coll)}) (inc ctr))
    ))
(index-list-map-rec ["a","b","c"], [], 1)



;; general questions
;; what does my mana curve look like?
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

