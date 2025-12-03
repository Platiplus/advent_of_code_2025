(ns advent-of-code.day-3.joltage.main
  (:require [clojure.string :as str]))

;; Part 1: Find the maximum joltage for a bank (two digits)
(defn max-joltage-for-bank-part-1
  [bank-str]
  (let [digits (vec bank-str)
        len (count digits)]
    (apply max
           (for [i (range len)
                 j (range (inc i) len)]
             (let [first-digit (Character/getNumericValue (nth digits i))
                   second-digit (Character/getNumericValue (nth digits j))]
               (+ (* first-digit 10) second-digit))))))

;; Part 2: Find the maximum joltage for a bank (12 digits)
;; Used greedy stack to keep the highest digits
(defn max-joltage-for-bank-part-2
  [bank-str]
  (let [digits (vec bank-str)
        len (count digits)
        digits-to-keep 12
        to-remove (- len digits-to-keep)]
    (if (<= len digits-to-keep)
      (Long/parseLong (apply str digits))
      (loop [stack []
             i 0
             removed 0]
        (cond
          (>= i len)
          (let [final-stack (take digits-to-keep stack)]
            (Long/parseLong (apply str final-stack)))
          
          (>= removed to-remove)
          (let [remaining-digits (subvec digits i)
                final-stack (concat stack remaining-digits)]
            (Long/parseLong (apply str (take digits-to-keep final-stack))))
          
          (empty? stack)
          (recur (conj stack (nth digits i))
                 (inc i)
                 removed)
          
          (and (< removed to-remove)
               (> (Character/getNumericValue (nth digits i))
                  (Character/getNumericValue (peek stack))))
          (recur (pop stack)
                 i
                 (inc removed))
          
          :else
          (recur (conj stack (nth digits i))
                 (inc i)
                 removed))))))

(defn total-output-joltage-part-1
  [input]
  (let [banks (str/split-lines input)]
    (apply + (map max-joltage-for-bank-part-1 banks))))

(defn total-output-joltage-part-2
  [input]
  (let [banks (str/split-lines input)]
    (apply + (map max-joltage-for-bank-part-2 banks))))
