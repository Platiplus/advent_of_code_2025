(ns advent-of-code.day-4.printing-department.main
  (:require [clojure.string :as str]))

(defn parse-grid
  [input]
  (mapv vec (str/split-lines input)))

(defn get-neighbors
  [row col grid-height grid-width]
  (for [row-offset [-1 0 1]
        col-offset [-1 0 1]
        :when (not (and (zero? row-offset) (zero? col-offset)))
        :let [neighbor-row (+ row row-offset)
              neighbor-col (+ col col-offset)]
        :when (and (>= neighbor-row 0)
                   (< neighbor-row grid-height)
                   (>= neighbor-col 0)
                   (< neighbor-col grid-width))]
    [neighbor-row neighbor-col]))

(defn count-adjacent-rolls
  [grid row col]
  (let [grid-height (count grid)
        grid-width (count (first grid))
        neighbor-positions (get-neighbors row col grid-height grid-width)]
    (count (filter (fn [[neighbor-row neighbor-col]]
                      (= \@ (get-in grid [neighbor-row neighbor-col])))
                    neighbor-positions))))

(defn printing-department-part-1
  [input]
  (let [grid (parse-grid input)
        num-rows (count grid)
        num-cols (count (first grid))]
    (loop [current-row 0
           current-col 0
           accessible-rolls-count 0]
      (cond
        (>= current-row num-rows) accessible-rolls-count
        (>= current-col num-cols) (recur (inc current-row) 0 accessible-rolls-count)
        
        (= \@ (get-in grid [current-row current-col]))
        (let [adjacent-rolls-count (count-adjacent-rolls grid current-row current-col)]
          (if (< adjacent-rolls-count 4)
            (recur current-row (inc current-col) (inc accessible-rolls-count))
            (recur current-row (inc current-col) accessible-rolls-count)))
        :else (recur current-row (inc current-col) accessible-rolls-count)))))

(defn find-accessible-rolls
  [grid]
  (let [num-rows (count grid)
        num-cols (count (first grid))]
    (for [row (range num-rows)
          col (range num-cols)
          :when (= \@ (get-in grid [row col]))
          :let [adjacent-rolls-count (count-adjacent-rolls grid row col)]
          :when (< adjacent-rolls-count 4)]
      [row col])))

(defn remove-rolls
  [grid positions-to-remove]
  (reduce (fn [current-grid [row col]]
            (assoc-in current-grid [row col] \.))
          grid
          positions-to-remove))

(defn printing-department-part-2
  [input]
  (let [initial-grid (parse-grid input)]
    (loop [current-grid initial-grid
           total-removed-count 0]
      (let [accessible-positions (find-accessible-rolls current-grid)]
        (if (empty? accessible-positions)
          total-removed-count
          (let [removed-this-round (count accessible-positions)
                grid-after-removal (remove-rolls current-grid accessible-positions)]
            (recur grid-after-removal (+ total-removed-count removed-this-round))))))))