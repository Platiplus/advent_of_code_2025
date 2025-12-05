(ns advent-of-code.day-5.cafeteria.main
  (:require [clojure.string :as str]
            [advent-of-code.utils.main :as utils]))

(defn parse-range
  [range-str]
  (let [[start end] (str/split range-str #"-")]
    [(Long/parseLong start) (Long/parseLong end)]))

(defn in-range?
  [id [start end]]
  (<= start id end))

(defn is-fresh?
  [id ranges]
  (some (partial in-range? id) ranges))

(defn count-fresh-ingredients-part-1
  [input]
  (let [parts (str/split input #"\n\n")
        range-lines (str/split-lines (first parts))
        ranges (map parse-range range-lines)
        id-lines (str/split-lines (second parts))
        ingredient-ids (map #(Long/parseLong %) id-lines)]
    (count (filter #(is-fresh? % ranges) ingredient-ids))))

(defn merge-ranges
  [ranges]
  (let [sorted-ranges (sort-by first ranges)]
    (loop [merged []
           remaining sorted-ranges]
      (if (empty? remaining)
        merged
        (let [[current-start current-end] (first remaining)
              [next-start next-end :as next-range] (first (rest remaining))]
          (if (or (empty? (rest remaining))
                  (> next-start (inc current-end)))
            (recur (conj merged [current-start current-end])
                   (rest remaining))
            (recur merged
                   (cons [current-start (max current-end next-end)]
                         (rest (rest remaining))))))))))

(defn range-size
  [[start end]]
  (inc (- end start)))

(defn count-fresh-ingredients-part-2
  [input]
  (let [parts (str/split input #"\n\n")
        range-lines (str/split-lines (first parts))
        ranges (map parse-range range-lines)
        merged-ranges (merge-ranges ranges)]
    (apply + (map range-size merged-ranges))))
