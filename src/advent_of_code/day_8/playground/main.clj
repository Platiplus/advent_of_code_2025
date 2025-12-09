(ns advent-of-code.day-8.playground.main
  (:require [clojure.string :as str]))

(defn parse-input
  [input]
  (->> (str/split-lines input)
       (map #(str/split % #",\s*"))
       (map (fn [coordinate-strings] (mapv #(Long/parseLong %) coordinate-strings)))
       (vec)))

(defn calculate-euclidian-distance
  [first-position second-position]
  (let [[x1 y1 z1] first-position
        [x2 y2 z2] second-position]
    (Math/sqrt (+ (Math/pow (- x1 x2) 2)
                  (Math/pow (- y1 y2) 2)
                  (Math/pow (- z1 z2) 2)))))

(defn make-union-find
  [num-junction-boxes]
  {:parent (vec (range num-junction-boxes))
   :rank (vec (repeat num-junction-boxes 0))})

(defn find-root
  [circuit-structure box-index]
  (let [parent-index (get-in circuit-structure [:parent box-index])]
    (if (= parent-index box-index)
      box-index
      (find-root circuit-structure parent-index))))

(defn union
  [circuit-structure first-box-index second-box-index]
  (let [root-of-first (find-root circuit-structure first-box-index)
        root-of-second (find-root circuit-structure second-box-index)]
    (if (= root-of-first root-of-second)
      circuit-structure
      (let [rank-of-first (get-in circuit-structure [:rank root-of-first])
            rank-of-second (get-in circuit-structure [:rank root-of-second])]
        (cond
          (< rank-of-first rank-of-second)
          (assoc-in circuit-structure [:parent root-of-first] root-of-second)
          (> rank-of-first rank-of-second)
          (assoc-in circuit-structure [:parent root-of-second] root-of-first)
          :else
          (-> circuit-structure
              (assoc-in [:parent root-of-second] root-of-first)
              (update-in [:rank root-of-first] inc)))))))

(defn in-same-circuit?
  [circuit-structure first-box-index second-box-index]
  (= (find-root circuit-structure first-box-index) (find-root circuit-structure second-box-index)))

(defn get-circuit-sizes
  [circuit-structure num-junction-boxes]
  (let [root-of-each-box (map #(find-root circuit-structure %) (range num-junction-boxes))]
    (->> root-of-each-box
         frequencies
         vals
         sort
         reverse)))

(defn all-in-same-circuit?
  [circuit-structure num-junction-boxes]
  (let [roots (map #(find-root circuit-structure %) (range num-junction-boxes))
        unique-roots (set roots)]
    (= 1 (count unique-roots))))

(defn prepare-pairs
  [junction-box-positions]
  (let [num-junction-boxes (count junction-box-positions)
        all-pairs (for [first-box-index (range num-junction-boxes)
                        second-box-index (range (inc first-box-index) num-junction-boxes)]
                    {:first-box-index first-box-index
                     :second-box-index second-box-index
                     :distance (calculate-euclidian-distance (nth junction-box-positions first-box-index)
                                                             (nth junction-box-positions second-box-index))})]
    {:pairs-sorted-by-distance (sort-by :distance all-pairs)
     :num-junction-boxes num-junction-boxes
     :junction-box-positions junction-box-positions}))

(defn connect-pairs
  [pairs-sorted-by-distance num-junction-boxes junction-box-positions stop-condition on-connection]
  (loop [circuit-structure (make-union-find num-junction-boxes)
         remaining-pairs pairs-sorted-by-distance
         pairs-processed 0
         connection-data nil]
    (if (or (empty? remaining-pairs) (stop-condition circuit-structure pairs-processed))
      {:circuit-structure circuit-structure
       :connection-data connection-data}
      (let [current-pair (first remaining-pairs)
            first-box-index (:first-box-index current-pair)
            second-box-index (:second-box-index current-pair)]
        (if (in-same-circuit? circuit-structure first-box-index second-box-index)
          (recur circuit-structure (rest remaining-pairs) (inc pairs-processed) connection-data)
          (let [updated-circuit-structure (union circuit-structure first-box-index second-box-index)
                new-connection-data (on-connection junction-box-positions first-box-index second-box-index)]
            (recur updated-circuit-structure (rest remaining-pairs) (inc pairs-processed) new-connection-data)))))))

(defn playground-part-1
  [input]
  (let [junction-box-positions (parse-input input)
        {:keys [pairs-sorted-by-distance num-junction-boxes]} (prepare-pairs junction-box-positions)
        num-pairs-to-process (if (= num-junction-boxes 20) 10 1000)
        stop-condition (fn [_ pairs-processed] (>= pairs-processed num-pairs-to-process))
        on-connection (fn [_ _ _] nil)
        {:keys [circuit-structure]} (connect-pairs pairs-sorted-by-distance num-junction-boxes junction-box-positions stop-condition on-connection)
        circuit-size-list (get-circuit-sizes circuit-structure num-junction-boxes)
        three-largest-circuits (take 3 circuit-size-list)]
    (apply * three-largest-circuits)))

(defn playground-part-2
  [input]
  (let [junction-box-positions (parse-input input)
        {:keys [pairs-sorted-by-distance num-junction-boxes]} (prepare-pairs junction-box-positions)
        stop-condition (fn [circuit-structure _] (all-in-same-circuit? circuit-structure num-junction-boxes))
        on-connection (fn [positions first-box-index second-box-index]
                        (let [[x1] (nth positions first-box-index)
                              [x2] (nth positions second-box-index)]
                          {:x1 x1 :x2 x2}))
        {:keys [connection-data]} (connect-pairs pairs-sorted-by-distance num-junction-boxes junction-box-positions stop-condition on-connection)]
    (* (:x1 connection-data) (:x2 connection-data))))