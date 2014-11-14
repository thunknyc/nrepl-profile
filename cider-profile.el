;;; cider-profile.el --- CIDER support for thunknyc/nrepl-profile -*- lexical-binding: t -*-

;; Copyright © 2014 Edwin Watkeys
;;
;; Author: Edwin Watkeys <edw@poseur.com>
;; Version: 0.1.0
;; Package-Requires: ((cider "0.8.0"))
;; Keywords: cider, clojure, profiling
;; URL: http://github.com/thunknyc/nrepl-profile

;; This program is free software: you can redistribute it and/or modify
;; it under the terms of the GNU General Public License as published by
;; the Free Software Foundation, either version 3 of the License, or
;; (at your option) any later version.

;; This program is distributed in the hope that it will be useful,
;; but WITHOUT ANY WARRANTY; without even the implied warranty of
;; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
;; GNU General Public License for more details.

;; You should have received a copy of the GNU General Public License
;; along with this program.  If not, see <http://www.gnu.org/licenses/>.

;; This file is not part of GNU Emacs.

;;; Commentary.

;; This package augments CIDER to provide coarse-grained interactive
;; profiling support.

(require 'cider)

;;;###autoload
(defun cider-profile-toggle (query)
  "Toggle profiling for the given QUERY.
Defaults to the symbol at point.  With prefix arg or no symbol at
point, prompts for a var."
  (interactive "P")
  (cider-ensure-op-supported "toggle-profile")
  (cider-read-symbol-name
   "Toggle profiling for var: "
   (lambda (sym)
     (let ((ns (cider-current-ns)))
       (nrepl-send-request
        (list "op" "toggle-profile"
              "ns" ns
              "sym" sym)
        (nrepl-make-response-handler
         (current-buffer)
         (lambda (_buffer value)
           (cond ((equal value "profiled")
                  (message (format "profiling %s/%s." ns sym)))
                 ((equal value "unprofiled")
                  (message (format "not profiling %s/%s." ns sym)))
                 ((equal value "unbound")
                  (message (format "%s/%s is not bound." ns sym)))))
         '()
         '()
         '()))))
   query))

;;;###autoload
(defun cider-profile-summary (query)
  "Display a summary of currently collected profile data."
  (interactive "P")
  (cider-ensure-op-supported "profile-summary")
  (nrepl-send-request
   (list "op" "profile-summary")
   (cider-interactive-eval-handler (current-buffer)))
  query)

;;;###autoload
(defun cider-profile-clear (query)
  "Clear any collected profile data."
  (interactive "P")
  (cider-ensure-op-supported "clear-profile")
  (nrepl-send-request
   (list "op" "clear-profile")
   (nrepl-make-response-handler
    (current-buffer)
    (lambda (_buffer value)
      (when (equal value "cleared")
        (message "cleared profile data.")))
    '()
    '()
    '()))
  query)

(define-minor-mode cider-profile-mode
  "Toggle cider-profile-mode."
  nil
  nil
  `((,(kbd "C-c M-=") . cider-profile-toggle)
    (,(kbd "C-c M-_") . cider-profile-clear)
    (,(kbd "C-c M--") . cider-profile-summary)))

(provide 'cider-profile)

;;; cider-profile.el ends here
