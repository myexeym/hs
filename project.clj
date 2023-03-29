(defproject sample "0.1.0-SNAPSHOT"
  :description "HS"
  :url "http://myexe.su"

  :dependencies [;; Clojure
                 [org.clojure/clojure "1.11.1"]

                 [org.clojure/java.jdbc "0.7.12"]
                 [org.postgresql/postgresql "42.5.4"]

                 [clojure.java-time "1.2.0"]

                 [integrant "0.8.0"]
                 [compojure "1.7.0"]
                 [ring "1.9.5"]
                 [ring-middleware-format "0.7.5"]
                 [ring/ring-headers "0.3.0"]
                 [ring-cors "0.1.13"]
                 [org.slf4j/slf4j-api "1.7.30"]
                 [org.slf4j/slf4j-simple "1.7.36"]
                 [org.clojure/tools.logging "1.2.4"]

                 [medley "1.4.0"]
                 [slingshot "0.12.2"]

                 ;; Clojure Script

                 [org.clojure/clojurescript "1.11.54"]

                 [thheller/shadow-cljs "2.19.9"]
                 [metosin/reitit-frontend "0.5.18"]
                 [reagent "1.1.1"]
                 [re-frame "1.2.0"]

                 [noencore "0.3.7"]
                 [cljs-http "0.1.46" :exclusions [noencore]]

                 [arttuka/reagent-material-ui "5.11.12-0"]
                 [com.andrewmcveigh/cljs-time "0.5.2"]

                 ;; Clojure and ClojureScript

                 [tongue "0.4.4"]
                 ]

  :source-paths ["src/clj" "src/cljs" "src/cljc"]
  :test-paths ["test/clj" "test/cljs" "test/cljc"]
  :resource-paths ["src/resources"]

  :aliases {"dev"
            ["do"
             "compile"
             ["run" "-m" "su.myexe.init"]]})


