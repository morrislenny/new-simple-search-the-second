(ns simple-search.core
  (:use simple-search.knapsack-examples.knapPI_11_20_1000
        simple-search.knapsack-examples.knapPI_13_20_1000
        simple-search.knapsack-examples.knapPI_16_20_1000))

;;; An answer will be a map with (at least) four entries:
;;;   * :instance
;;;   * :choices - a vector of 0's and 1's indicating whether
;;;        the corresponding item should be included
;;;   * :total-weight - the weight of the chosen items
;;;   * :total-value - the value of the chosen items

(defn included-items
  "Takes a sequences of items and a sequence of choices and
  returns the subsequence of items corresponding to the 1's
  in the choices sequence."
  [items choices]
  (map first
       (filter #(= 1 (second %))
               (map vector items choices))))

(defn random-answer
  "Construct a random answer for the given instance of the
  knapsack problem."
  [instance]
  (let [choices (repeatedly (count (:items instance))
                            #(rand-int 2))
        included (included-items (:items instance) choices)]
    {:instance instance
     :choices (vec choices)
     :total-weight (reduce + (map :weight included))
     :total-value (reduce + (map :value included))}))

;;; It might be cool to write a function that
;;; generates weighted proportions of 0's and 1's.

(defn score
  "Takes the total-weight of the given answer unless it's over capacity,
   in which case we return 0."
  [answer]
  (if (> (:total-weight answer)
         (:capacity (:instance answer)))
    0
    (:total-value answer)))

(defn add-score
  "Computes the score of an answer and inserts a new :score field
   to the given answer, returning the augmented answer."
  [answer]
  (assoc answer :score (score answer)))

"(defn hill-search
  [instance max-tries]
  (apply max-key :score
         (map add-score
              (tweaker max-tries #(random-answer instance)))))"

"{:instance {:capacity 994, :items ({:value 403N, :weight 94N} {:value 886N, :weight 506N} {:value 814N, :weight 416N} {:value 1151N, :weight 992N}
{:value 983N, :weight 649N} {:value 629N, :weight 237N} {:value 848N, :weight 457N} {:value 1074N, :weight 815N} {:value 839N, :weight 446N}
{:value 819N, :weight 422N} {:value 1062N, :weight 791N} {:value 762N, :weight 359N} {:value 994N, :weight 667N} {:value 950N, :weight 598N}
{:value 111N, :weight 7N} {:value 914N, :weight 544N} {:value 737N, :weight 334N} {:value 1049N, :weight 766N} {:value 1152N, :weight 994N}
{:value 1110N, :weight 893N})}, :choices (0 0 1 0 0 1 0 0 0 0 0 0 0 0 1 0 1 0 0 0), :total-weight 994N, :total-value 2291N, :score 2291N}"

(defn remove-item
  [instance]
  (let [index (rand-int (count (:items instance)))]
    (if (== (nth (:choices instance) index) 1)
    (assoc (:choices instance) index 0)
    (trampoline remove-item instance))))

"(defn tweaker
  [instance max-tries]
  (if (> :score (score instance) 0)
    "Add item"
    "Remove item: pick one random from :choices, if 1, change it to zero, and score"))

(time (hill-search knapPI_16_20_1000_1 100
))"

(defn random-search
  [instance max-tries]
  (apply max-key :score
         (map add-score
              (repeatedly max-tries #(random-answer instance)))))

 (let [index (rand-int (count (:items (random-search knapPI_16_20_1000_1 100))))]
    (if (== (nth (:choices (random-search knapPI_16_20_1000_1 100)) index) 1)
    (assoc (:choices (random-search knapPI_16_20_1000_1 100)) index 0)))

(remove-item (random-search knapPI_16_20_1000_1 100
))
