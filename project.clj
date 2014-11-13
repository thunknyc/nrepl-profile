(defproject thunknyc/nrepl-profile "0.1.0-SNAPSHOT"
  :description "nrepl support for thunknyc/progile"
  :url "http://github.com/thunknyc/nrepl-profile"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[thunknyc/profile "0.3.8"]]
  :exclusions [org.clojure/clojure]
  :profiles {:1.5 {:dependencies [[org.clojure/clojure "1.5.1"]]}
             :1.6 {:dependencies [[org.clojure/clojure "1.6.0"]]}
             :1.7 {:dependencies [[org.clojure/clojure "1.7.0-master-SNAPSHOT"]]}
             :dev {:repl-options {:nrepl-middleware [nrepl-profile.core/wrap-profile]}}})
