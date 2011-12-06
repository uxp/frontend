(ns circle.init
  (:require circle.env) ;; env needs to be loaded before any circle source files containing tests 
  (:require circle.swank)
  (:require circle.db)
  (:require circle.repl)
  (:require circle.logging)
  (:require circle.util.chdir)
  (:require circle.backend.build.run)
  (:require circle.backend.build.config)
  (:require fs))

(defn maybe-change-dir
  "Change the current working directory to backend/. Although changing it to the project root makes
  more conceptual sense, there are lots of entrypoints to the clojure code (for example, tests,
  swank, etc) which are hard to get a hook on, but making sure there is a hook into the Rails code
  is easy. It makes more code to write this in JRuby, but it's written now, so why change it.cl" []
  (when (= (fs/exists? "Gemfile"))
    (circle.util.chdir/chdir "backend")
    (println "Changing current working directory to" (fs/abspath (fs/cwd)))))

(defonce init*
  (delay
   (try
     (println "circle.init/init")
     (circle.logging/init)
     (when (System/getenv "CIRCLE_SWANK")
       (circle.swank/init))
     (circle.db/init)
     (circle.repl/init)
     (println (java.util.Date.))
     true
     (catch Exception e
       (println "caught exception on startup:")
       (.printStackTrace e)
       (println "exiting")
       (System/exit 1)))))

(defn init
  "Start everything up. idempotent."
  []
  @init*)

(defn -main []
  (init))