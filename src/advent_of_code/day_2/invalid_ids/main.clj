(ns advent-of-code.day-2.invalid-ids.main
  (:require [clojure.string :as str]))

(defn get-divisors
  [num]
  (let [candidates (range 2 (inc num))]
    (filter (fn [divisor] (zero? (mod num divisor))) candidates)))

(defn parse-range
  [range-str]
  (let [parts (str/split range-str #"-")]
    [(Long/parseLong (first parts))
     (Long/parseLong (second parts))]))

;; A number is invalid if it has an even number of digits and the first half
;; is exactly the same as the second half (like 1212 or 3333)
(defn is-invalid-id-part-1?
  [num]
  (let [num-str (str num)
        len (count num-str)]
    (if (even? len)
      (let [half-len (/ len 2)
            first-half (subs num-str 0 half-len)
            second-half (subs num-str half-len)]
        (= first-half second-half))
      false)))

;; A number is invalid if it can be split into equal repeating parts
;; We check all possible ways to divide the number based on its length's divisors
(defn is-invalid-id-part-2?
  [num]
  (let [num-str (str num)
        len (count num-str)]
    (let [divisors (get-divisors len)]
      (some? (some (fn [num-parts]
                     (let [part-length (/ len num-parts)
                           first-part (subs num-str 0 part-length)
                           all-parts-match? (every? (fn [i]
                                                       (let [start (* i part-length)
                                                             end (+ start part-length)
                                                             part (subs num-str start end)]
                                                         (= part first-part)))
                                                     (range num-parts))]
                       (when all-parts-match? true)))
                   divisors)))))

(defn sum-invalid-ids-part-1
  [input]
  (let [range-strings (str/split input #",")
        invalid-ids (mapcat (fn [range-str]
                              (let [[start end] (parse-range range-str)
                                    all-numbers (range start (inc end))]
                                (filter is-invalid-id-part-1? all-numbers)))
                            range-strings)]
    (apply + invalid-ids)))

(defn sum-invalid-ids-part-2
  [input]
  (let [range-strings (str/split input #",")
        invalid-ids (mapcat (fn [range-str]
                              (let [[start end] (parse-range range-str)
                                    all-numbers (range start (inc end))]
                                (filter is-invalid-id-part-2? all-numbers)))
                            range-strings)]
    (apply + invalid-ids)))
