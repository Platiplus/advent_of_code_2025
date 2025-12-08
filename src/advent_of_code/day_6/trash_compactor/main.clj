(ns advent-of-code.day-6.trash-compactor.main
  (:require [clojure.string :as str]))

(defn parse-columns
  [lines]
  (let [max-length (apply max (map count lines))
        num-lines (dec (count lines))
        operator-line (last lines)]
    (loop [col 0
           columns []]
      (if (>= col max-length)
        columns
        (let [col-chars (map #(get % col \space) lines)
              is-separator? (every? #(= \space %) col-chars)]
          (if is-separator?
            (recur (inc col) columns)
            (let [problem-columns (loop [c col
                                         cols []]
                                    (if (>= c max-length)
                                      cols
                                      (let [chars (map #(get % c \space) lines)
                                            is-sep? (every? #(= \space %) chars)]
                                        (if is-sep?
                                          cols
                                          (recur (inc c) (conj cols chars))))))
                  problem-width (count problem-columns)
                  numbers (for [row (range num-lines)]
                            (let [row-chars (map #(nth % row) problem-columns)
                                  all-chars (apply str row-chars)
                                  digits (apply str (re-seq #"\d" all-chars))]
                              (when (not (str/blank? digits))
                                (Long/parseLong digits))))
                  operator-char (get operator-line col)
                  operator (case operator-char
                             \+ +
                             \* *
                             nil)]
              (recur (+ col problem-width) (conj columns {:numbers (filter some? numbers)
                                                          :operator operator})))))))))

(defn solve-problem
  [{:keys [numbers operator]}]
  (apply operator numbers))

(defn trash-compactor-part-1
  [input]
  (let [lines (str/split-lines input)
        columns (parse-columns lines)
        results (map solve-problem columns)]
    (apply + results)))

(defn parse-columns-right-to-left
  [lines]
  (let [max-length (apply max (map count lines))
        operator-line (last lines)]
    (loop [col 0
           columns []]
      (if (>= col max-length)
        columns
        (let [col-chars (map #(get % col \space) lines)
              is-separator? (every? #(= \space %) col-chars)]
          (if is-separator?
            (recur (inc col) columns)
            (let [problem-columns (loop [c col
                                         cols []]
                                    (if (>= c max-length)
                                      cols
                                      (let [chars (map #(get % c \space) lines)
                                            is-sep? (every? #(= \space %) chars)]
                                        (if is-sep?
                                          cols
                                          (recur (inc c) (conj cols chars))))))
                  problem-width (count problem-columns)
                  numbers (for [col-idx (range (dec problem-width) -1 -1)]
                            (let [column-chars (nth problem-columns col-idx)
                                  digits (apply str (filter #(Character/isDigit %) column-chars))]
                              (when (not (str/blank? digits))
                                (Long/parseLong digits))))
                  operator-char (get operator-line col)
                  operator (case operator-char
                             \+ +
                             \* *
                             nil)]
              (recur (+ col problem-width) (conj columns {:numbers (filter some? numbers)
                                                          :operator operator})))))))))

(defn trash-compactor-part-2
  [input]
  (let [lines (str/split-lines input)
        columns (parse-columns-right-to-left lines)
        results (map solve-problem columns)]
    (apply + results)))