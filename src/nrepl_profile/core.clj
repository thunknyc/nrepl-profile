(ns nrepl-profile.core
  (:require [clojure.string :as s]
            [profile.core :as p]
            [clojure.tools.nrepl.transport :as t]
            [clojure.tools.nrepl.middleware :refer [set-descriptor!]]
            [clojure.tools.nrepl.misc :refer [response-for]]
            [cider.nrepl.middleware.util.misc :as u]))

(defn send-exception
  [e msg transport]
  (t/send transport (response-for msg :status :done :value "exception")))

(defn toggle-profile
  [{:keys [ns sym transport] :as msg}]
  (try
    (if-let [v (ns-resolve (symbol ns) (symbol sym))]
      (let [profiled? (p/toggle-profile-var* v)]
        (t/send transport
                (response-for
                 msg
                 :status :done
                 :value (if profiled? "profiled" "unprofiled"))))
      (t/send transport
              (response-for
               msg
               :status #{:toggle-profile-not-such-var :done}
               :value "unbound")))
    (catch Exception e (send-exception e msg transport))))

(defn profile-summary
  [{:keys [transport] :as msg}]
  (try
    (t/send transport
            (response-for msg
                          :status :done
                          :err (with-out-str
                                 (binding [*err* *out*] (p/print-summary)))))
    (catch Exception e (send-exception e msg transport))))

(defn clear-profile
  [{:keys [transport] :as msg}]
  (try
    (p/clear-profile-data)
    (t/send transport
            (response-for msg
                          :status :done
                          :value "cleared"))
    (catch Exception e (send-exception e msg transport))))

(defn toggle-profile-ns
  [{:keys [ns transport] :as msg}]
  (try (let [profiled? (p/toggle-profile-ns (symbol ns))]
         (t/send transport
                 (response-for
                  msg
                  :status :done
                  :value (if profiled? "profiled" "unprofiled"))))
       (catch Exception e (send-exception e msg transport))))

(defn is-var-profiled
  [{:keys [ns sym transport] :as msg}]
  (try (let [var (ns-resolve (symbol ns) (symbol sym))
             profiled? (p/profiled? @var)]
         (t/send transport
                 (response-for
                  msg
                  :status :done
                  :value (if profiled? "profiled" "unprofiled"))))
       (catch Exception e (send-exception e msg transport))))

(defn get-max-samples
  [{:keys [transport] :as msg}]
  (try (t/send transport
               (response-for
                msg
                :status :done
                :value (str (p/max-sample-count))))
       (catch Exception e (send-exception e msg transport))))

(defn normalize-max-samples [n]
  (cond (and (sequential? n) (empty? n)) nil
        (string? n) (Long/parseLong n)
        :else n))

(defn set-max-samples
  [{:keys [max-samples transport] :as msg}]
  (try (let [max-samples (normalize-max-samples max-samples)]
         (p/set-max-sample-count max-samples)
         (t/send transport
                 (response-for
                  msg
                  :status :done
                  :value (str (p/max-sample-count)))))
       (catch Exception e  (send-exception e msg transport))))

(defn wrap-profile
  "Middleware that toggles profiling of a given var."
  [handler]
  (fn [{:keys [op] :as msg}]
    (case op
      "toggle-profile"
      (toggle-profile msg)
      "toggle-profile-ns"
      (toggle-profile-ns msg)
      "is-var-profiled"
      (is-var-profiled msg)
      "profile-summary"
      (profile-summary msg)
      "clear-profile"
      (clear-profile msg)
      "get-max-samples"
      (get-max-samples msg)
      "set-max-samples"
      (set-max-samples msg)
      (handler msg))))

(set-descriptor!
 #'wrap-profile
 {:handles
  {"toggle-profile-ns"
   {:doc "Toggle profiling of given namespace."
    :requires {"ns" "The current namespace"}
    :returns {"status" "Done"
              "value" "'profiled' if profiling enabled, 'unprofiled' if disabled"}}
   "is-var-profiled"
   {:doc "Reports wheth symbol is currently profiled."
    :requires {"sym" "The symbol to check"
               "ns" "The current namespace"}
    :returns {"status" "Done"
              "value" "'profiled' if profiling enabled, 'unprofiled' if disabled"}}
   "get-max-samples"
   {:doc "Returns maximum number of samples to be collected for any var."
    :requires {}
    :returns {"status" "Done"
              "value" "String representing number of max-sample-count"}}
   "set-max-samples"
   {:doc "Sets maximum sample count. Returns new max-sample-count."
    :requires {"max-samples" "Maxiumum samples to collect for any single var."}
    :returns {"status" "Done"
              "value" "String representing number of max-sample-count"}}
   "toggle-profile"
   {:doc "Toggle profiling of a given var."
    :requires {"sym" "The symbol to profile"
               "ns" "The current namespace"}
    :returns {"status" "Done"
              "value" "'profiled' if profiling enabled, 'unprofiled' if disabled, 'unbound' if ns/sym not bound"}}
   "profile-summary"
   {:doc "Return profiling data summary."
    :requires {}
    :returns {"status" "Done"
              "err" "Content of profile summary report"}}
   "clear-profile"
   {:doc "Clears profile of samples."
    :requires {}
    :returns {"status" "Done"}}}})
