(ns advent-of-code.day-7.laboratories.main
  (:require [clojure.string :as str]))

(defn parse-grid
  [input]
  (let [lines (str/split-lines input)
        grid (vec (map vec lines))
        start-pos (first (for [row (range (count lines))
                                col (range (count (nth lines row)))
                                :when (= \S (get-in grid [row col]))]
                            [row col]))]
    {:grid grid
     :start-pos start-pos}))

(defn get-cell
  [grid [row col]]
  (when (and (>= row 0)
             (< row (count grid))
             (>= col 0)
             (< col (count (nth grid row))))
    (get-in grid [row col])))

(defn simulate-beams
  [grid start-pos]
  (loop [active-beams #{start-pos}
         split-count 0]
    (if (empty? active-beams)
      split-count
      (let [new-beams (atom #{})
            new-splits (atom 0)]
        (doseq [[row col] active-beams]
          (let [next-row (inc row)
                next-pos [next-row col]
                next-cell (get-cell grid next-pos)]
            (cond
              (nil? next-cell)
              nil
              (= \^ next-cell)
              (do
                (swap! new-splits inc)
                (let [left-pos [next-row (dec col)]
                      right-pos [next-row (inc col)]]
                  (when (get-cell grid left-pos)
                    (swap! new-beams conj left-pos))
                  (when (get-cell grid right-pos)
                    (swap! new-beams conj right-pos))))
              :else
              (swap! new-beams conj next-pos))))
        (recur @new-beams (+ split-count @new-splits))))))

(defn laboratories-part-1
  [input]
  (let [{:keys [grid start-pos]} (parse-grid input)]
    (if start-pos
      (simulate-beams grid start-pos)
      0)))

(defn simulate-quantum-timelines
  [grid start-pos]
  (let [max-row (count grid)
        timeline-counts (atom {start-pos 1})
        exit-counts (atom 0)]
    (loop [current-row (inc (first start-pos))]
      (when (< current-row max-row)
        (let [new-counts (atom {})]
          (doseq [[[prev-row prev-col] count] @timeline-counts
                  :when (= prev-row (dec current-row))]
            (let [current-pos [current-row prev-col]
                  current-cell (get-cell grid current-pos)]
              (cond
                (nil? current-cell)
                (swap! exit-counts + count)
                (= \^ current-cell)
                (do
                  (let [splitter-col prev-col
                        left-pos [current-row (dec splitter-col)]
                        right-pos [current-row (inc splitter-col)]]
                    (if (get-cell grid left-pos)
                      (swap! new-counts update left-pos (fnil + 0) count)
                      (swap! exit-counts + count))
                    (if (get-cell grid right-pos)
                      (swap! new-counts update right-pos (fnil + 0) count)
                      (swap! exit-counts + count))))
                :else
                (swap! new-counts update current-pos (fnil + 0) count))))
          (reset! timeline-counts @new-counts)
          (recur (inc current-row)))))
    
    (doseq [[[row col] count] @timeline-counts]
      (let [next-pos [(inc row) col]
            next-cell (get-cell grid next-pos)]
        (when (nil? next-cell)
          (swap! exit-counts + count))
        (let [current-cell (get-cell grid [row col])]
          (when (= \^ current-cell)
            (let [left-pos [row (dec col)]
                  right-pos [row (inc col)]]
              (when (nil? (get-cell grid left-pos))
                (swap! exit-counts + count))
              (when (nil? (get-cell grid right-pos))
                (swap! exit-counts + count)))))))
    @exit-counts))

(defn laboratories-part-2
  [input]
  (let [{:keys [grid start-pos]} (parse-grid input)]
    (if start-pos
      (simulate-quantum-timelines grid start-pos)
      0)))