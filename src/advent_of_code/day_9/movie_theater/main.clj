(ns advent-of-code.day-9.movie-theater.main
  (:require [clojure.string :as str]))

(defn parse-coordinate
  [line]
  (let [[x-str y-str] (str/split line #",")]
    [(Integer/parseInt x-str) (Integer/parseInt y-str)]))

(defn parse-red-tiles
  [input]
  (->> input
       str/split-lines
       (map parse-coordinate)))

(defn rectangle-area
  [[x1 y1] [x2 y2]]
  (* (inc (Math/abs (- x2 x1)))
     (inc (Math/abs (- y2 y1)))))

(defn can-form-opposite-corners?
  [[x1 y1] [x2 y2]]
  (and (not= x1 x2)
       (not= y1 y2)))

(defn find-largest-rectangle
  [red-tiles]
  (let [all-pairs (for [tile1 red-tiles
                        tile2 red-tiles
                        :when (and (not= tile1 tile2)
                                   (can-form-opposite-corners? tile1 tile2))]
                    [tile1 tile2])]
    (if (empty? all-pairs)
      0
      (->> all-pairs
           (map (fn [[corner1 corner2]]
                  (rectangle-area corner1 corner2)))
           (apply max)))))

(defn movie-theater-part-1
  [input]
  (let [red-tiles (parse-red-tiles input)
        largest-area (find-largest-rectangle red-tiles)]
    largest-area))

;; Part 2

(defn build-vertical-edges
  [red-tiles]
  (let [tiles-vec (vec red-tiles)
        tile-count (count tiles-vec)]
    (vec
     (for [tile-index (range tile-count)
           :let [[current-x current-y] (tiles-vec tile-index)
                 [next-x next-y] (tiles-vec (mod (inc tile-index) tile-count))] ;; Wrap around: last connects to first
           :when (= current-x next-x)] ;; Only vertical segments (same x-coordinate)
       [current-x (min current-y next-y) (max current-y next-y)])))) ;; Return [x, y-min, y-max]

(defn build-horizontal-edges
  [red-tiles]
  (let [tiles-vec (vec red-tiles)
        tile-count (count tiles-vec)]
    (vec
     (for [tile-index (range tile-count)
           :let [[current-x current-y] (tiles-vec tile-index)
                 [next-x next-y] (tiles-vec (mod (inc tile-index) tile-count))] ;; Wrap around: last connects to first
           :when (= current-y next-y)] ;; Only horizontal segments (same y-coordinate)
       [current-y (min current-x next-x) (max current-x next-x)])))) ;; Return [y, x-min, x-max]

(defn valid-x-range-at-y
  [v-edges h-edges y]
  (let [crossings (->> v-edges
                       (filter (fn [[_ y-min y-max]]
                                 (and (<= y-min y) (< y y-max))))
                       (map first)
                       sort
                       vec)
        interior-ranges (partition 2 crossings)
        boundary-xs (->> h-edges
                         (filter (fn [[horizontal-edge-y _ _]] (= horizontal-edge-y y)))
                         (mapcat (fn [[_ edge-x-min edge-x-max]] [edge-x-min edge-x-max])))
        all-x (concat
               (mapcat (fn [[range-start-x range-end-x]] [range-start-x range-end-x]) interior-ranges)
               boundary-xs)]
    (when (seq all-x)
      [(apply min all-x) (apply max all-x)])))

(defn compute-unique-y-ranges
  [red-tiles]
  (let [v-edges (build-vertical-edges red-tiles)
        h-edges (build-horizontal-edges red-tiles)
        unique-y-coordinates (distinct (map second red-tiles))]
    (into {}
          (for [y-coordinate unique-y-coordinates
                :let [x-range (valid-x-range-at-y v-edges h-edges y-coordinate)]
                :when x-range]
            [y-coordinate x-range])))) ; Map each unique y-coordinate to its valid x-range

(defn rectangle-valid-for-part2?
  [y-ranges [corner1-x corner1-y] [corner2-x corner2-y]]
  (let [rect-x-min (min corner1-x corner2-x)
        rect-x-max (max corner1-x corner2-x)
        rect-y-min (min corner1-y corner2-y)
        rect-y-max (max corner1-y corner2-y)
        unique-y-coordinates-in-range (filter #(and (>= % rect-y-min) (<= % rect-y-max))
                                              (keys y-ranges))]
    (every? (fn [y-coordinate]
              (when-let [[valid-x-min valid-x-max] (y-ranges y-coordinate)]
                (and (<= valid-x-min rect-x-min)
                     (>= valid-x-max rect-x-max))))
            unique-y-coordinates-in-range)))

(defn find-largest-valid-rectangle-part2
  [red-tiles]
  (let [tiles-vec (vec red-tiles)
        tile-count (count tiles-vec)
        ;; Precompute once: map of unique y-coordinate -> [x-min, x-max]
        y-ranges (compute-unique-y-ranges red-tiles)]
    (reduce max 0
            (for [first-tile-index (range tile-count)
                  second-tile-index (range (inc first-tile-index) tile-count) ;; Check all pairs (avoid duplicates)
                  :let [corner1 (tiles-vec first-tile-index)
                        corner2 (tiles-vec second-tile-index)
                        [corner1-x corner1-y] corner1
                        [corner2-x corner2-y] corner2]
                  :when (and (not= corner1-x corner2-x)  ;; Can form rectangle (different x-coords)
                             (not= corner1-y corner2-y)   ;; Can form rectangle (different y-coords)
                             (rectangle-valid-for-part2? y-ranges corner1 corner2))] ;; Valid region check
              (rectangle-area corner1 corner2)))))

(defn movie-theater-part-2
  [input]
  (let [red-tiles (parse-red-tiles input)]
    (find-largest-valid-rectangle-part2 red-tiles)))
