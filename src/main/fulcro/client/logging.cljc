(ns fulcro.client.logging
  "DEPRECATED: Will be removed in a future release."
  #?(:cljs (:require
             [goog.log :as glog]
             [goog.debug.Logger.Level :as level]))
  #?(:cljs (:import [goog.debug Console])))

#?(:clj
   (do
     (println "fulcro.client.logging is DEPRECATED. Please discontinue using it. If you're seeing this message
     then some Clojure code is requiring fulcro.client.logging. It is recommended that you use a dedicated logging
     library like timbre (clj/cljs).

     If you see an error related to taoensso.timbre, it is because that dependency is no longer included in Fulcro and
     you're using this deprecated logging.")
     (require 'taoensso.timbre)))

#?(:clj (def ^:dynamic *logger* (fn [this & args] (apply println args)))
   :cljs
        (defonce *logger*
          (when ^boolean goog.DEBUG
            (.setCapturing (Console.) true)
            (glog/getLogger "fulcro.client"))))

#?(:cljs
        (defn set-level [log-level]
          "Takes a keyword (:all, :debug, :info, :warn, :error, :none) and changes the log level accordingly.
          Note that the log levels are listed from least restrictive level to most restrictive."
          (.setLevel *logger*
            (level/getPredefinedLevel
              (case log-level :all "ALL" :debug "FINE" :info "INFO" :warn "WARNING" :error "SEVERE" :none "OFF"))))
   :clj (defn set-level [l] (taoensso.timbre/set-level! l)))

#?(:cljs
   (defn value-message
     "Include a pretty-printed cljs value as a string with the given text message."
     [msg val]
     (str msg ":\n" (pr-str val)))
   :clj
   (defn value-message [msg val] (str msg val)))


#?(:cljs
        (defn debug
          "Print a debug message to the logger which includes a value.
          Returns the value (like identity) so it can be harmlessly nested in expressions."
          ([value] (glog/fine *logger* (value-message "DEBUG" value)) value)
          ([msg value] (glog/fine *logger* (value-message msg value)) value))
   :clj (defn debug
          ([v] (taoensso.timbre/debug v))
          ([m v] (taoensso.timbre/debug m v))))

#?(:cljs
   (defn info
     "output an INFO level message to the logger"
     [& data]
     (glog/info *logger* (apply str (interpose " " data))))
   :clj
   (defn info [& data]
     (taoensso.timbre/info (apply str (interpose " " data)))))

#?(:cljs
        (defn warn
          "output a WARNING level message to the logger"
          [& data]
          (glog/warning *logger* (apply str (interpose " " data))))
   :clj (defn warn
          "output a WARNING level message to the logger"
          [& data]
          (taoensso.timbre/warn (apply str (interpose " " data)))))

#?(:cljs
        (defn error
          "output an ERROR level message to the logger"
          [& data]
          (glog/error *logger* (apply str (interpose " " data))))
   :clj (defn error [& data] (taoensso.timbre/error (apply str (interpose " " data)))))
