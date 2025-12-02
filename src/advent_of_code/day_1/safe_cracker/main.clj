(ns advent-of-code.day-1.safe-cracker.main
  (:gen-class)
  (:require [clojure.string :as str]))

;; Part 1: Count how many times we land exactly on zero after each rotation
(defn safe-cracker-part-1
  [rotations-string]
  (let [rotations (str/split-lines rotations-string)]
    (loop [rotations rotations
           position 50
           count 0]
      (if (empty? rotations)
        count
        (let [rotation (first rotations)
              direction (nth rotation 0)
              steps (Long/parseLong (subs rotation 1))
              final-position (case direction
                              \L (mod (- position steps) 100)
                              \R (mod (+ position steps) 100))
              zeros-counted (if (zero? final-position) 1 0)]
          (recur (rest rotations)
                 final-position
                 (+ count zeros-counted)))))))

;; Count how many zeros we pass through while rotating using math
(defn count-zeros-in-rotation
  [start steps direction]
  (if (zero? steps)
    0
    (case direction
      \L (let [range-start-float (/ (- start steps) 100.0)
               range-end-float (/ (dec start) 100.0)]
            (if (<= range-start-float range-end-float)
              (let [first-zero-multiple (long (Math/ceil range-start-float))
                    last-zero-multiple (long (Math/floor range-end-float))]
                (if (<= first-zero-multiple last-zero-multiple)
                  (inc (- last-zero-multiple first-zero-multiple))
                  0))
              0))
      \R (let [range-start-float (/ (inc start) 100.0)
               range-end-float (/ (+ start steps) 100.0)]
            (if (<= range-start-float range-end-float)
              (let [first-zero-multiple (long (Math/ceil range-start-float))
                    last-zero-multiple (long (Math/floor range-end-float))]
                (if (<= first-zero-multiple last-zero-multiple)
                  (inc (- last-zero-multiple first-zero-multiple))
                  0))
              0)))))

;; Part 2: Count all the zeros we encounter during each rotation, not just at the end
(defn safe-cracker-part-2
  [rotations-string]
  (let [rotations (str/split-lines rotations-string)]
    (loop [rotations rotations
           position 50
           count 0]
      (if (empty? rotations)
        count
        (let [rotation (first rotations)
              direction (nth rotation 0)
              steps (Long/parseLong (subs rotation 1))
              final-position (case direction
                              \L (mod (- position steps) 100)
                              \R (mod (+ position steps) 100))
              zeros-during-rotation (count-zeros-in-rotation position steps direction)]
          (recur (rest rotations)
                 final-position
                 (+ count zeros-during-rotation)))))))
