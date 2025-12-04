(ns advent-of-code.core
  (:gen-class)
  (:require [advent-of-code.utils.main :refer [read-input]]
            [advent-of-code.day-1.safe-cracker.main :refer [safe-cracker-part-1 safe-cracker-part-2]]
            [advent-of-code.day-2.invalid-ids.main :refer [sum-invalid-ids-part-1 sum-invalid-ids-part-2]]
            [advent-of-code.day-3.joltage.main :refer [total-output-joltage-part-1 total-output-joltage-part-2]]
            [advent-of-code.day-4.printing-department.main :refer [printing-department-part-1 printing-department-part-2]]))

(defn day-1 []
  (println "Advent of Code - Day 1")
  (println "Safe Cracker")
  (let [input (read-input "src/advent_of_code/day_1/safe_cracker/test-input.txt")]
    (println "Part 1 result:" (safe-cracker-part-1 input))
    (println "Part 2 result:" (safe-cracker-part-2 input))))

(defn day-2 []
  (println "--------------------------------")
  (println "Advent of Code - Day 2")
  (println "Invalid IDs")
  (let [input (read-input "src/advent_of_code/day_2/invalid_ids/test-input.txt")]
    (println "Part 1 result:" (sum-invalid-ids-part-1 input))
    (println "Part 2 result:" (sum-invalid-ids-part-2 input))))

(defn day-3 []
  (println "--------------------------------")
  (println "Advent of Code - Day 3")
  (println "Joltage")
  (let [input (read-input "src/advent_of_code/day_3/joltage/test-input.txt")]
    (println "Part 1 result:" (total-output-joltage-part-1 input))
    (println "Part 2 result:" (total-output-joltage-part-2 input))))

(defn day-4 []
  (println "--------------------------------")
  (println "Advent of Code - Day 4")
  (println "Printing Department")
  (let [input (read-input "src/advent_of_code/day_4/printing_department/test-input.txt")]
    (println "Part 1 result:" (printing-department-part-1 input))
    (println "Part 2 result:" (printing-department-part-2 input))))

(defn -main
  "Main entry point for Advent of Code solutions"
  [& args]
  (day-1)
  (day-2)
  (day-3)
  (day-4))
