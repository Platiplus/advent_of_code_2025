(ns advent-of-code.core
  (:gen-class)
  (:require [advent-of-code.utils.main :refer [read-input]]
            [advent-of-code.day-1.safe-cracker.main :refer [safe-cracker-part-1 safe-cracker-part-2]]
            [advent-of-code.day-2.invalid-ids.main :refer [sum-invalid-ids-part-1 sum-invalid-ids-part-2]]
            [advent-of-code.day-3.joltage.main :refer [total-output-joltage-part-1 total-output-joltage-part-2]]
            [advent-of-code.day-4.printing-department.main :refer [printing-department-part-1 printing-department-part-2]]
            [advent-of-code.day-5.cafeteria.main :refer [count-fresh-ingredients-part-1 count-fresh-ingredients-part-2]]
            [advent-of-code.day-6.trash-compactor.main :refer [trash-compactor-part-1 trash-compactor-part-2]]
            [advent-of-code.day-7.laboratories.main :refer [laboratories-part-1 laboratories-part-2]]))

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

(defn day-5 []
  (println "--------------------------------")
  (println "Advent of Code - Day 5")
  (println "Cafeteria")
  (let [input (read-input "src/advent_of_code/day_5/cafeteria/test-input.txt")]
    (println "Part 1 result:" (count-fresh-ingredients-part-1 input))
    (println "Part 2 result:" (count-fresh-ingredients-part-2 input))))

(defn day-6 []
  (println "--------------------------------")
  (println "Advent of Code - Day 6")
  (println "Trash Compactor")
  (let [input (read-input "src/advent_of_code/day_6/trash_compactor/test-input.txt")]
    (println "Part 1 result:" (trash-compactor-part-1 input))
    (println "Part 2 result:" (trash-compactor-part-2 input))))

(defn day-7 []
  (println "--------------------------------")
  (println "Advent of Code - Day 7")
  (println "Laboratories")
  (let [input (read-input "src/advent_of_code/day_7/laboratories/input.txt")]
    (println "Part 1 result:" (laboratories-part-1 input))
    (println "Part 2 result:" (laboratories-part-2 input))))

(defn -main
  "Main entry point for Advent of Code solutions"
  [& args]
  (day-1)
  (day-2)
  (day-3)
  (day-4)
  (day-5)
  (day-6)
  (day-7))