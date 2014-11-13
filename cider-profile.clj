;;; cider-profile.el --- CIDER support for thunknyc/nrepl-profile -*- lexical-binding: t -*-

;; Copyright © 2014 Edwin Watkeys
;;
;; Author: Edwin Watkeys <edw@poseur.com>

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

(require 'cider)

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

(defun cider-profile-summary (query)
  "Display a summary of currently collected profile data."
  (interactive "P")
  (cider-ensure-op-supported "profile-summary")
  (nrepl-send-request
   (list "op" "profile-summary")
   (cider-interactive-eval-handler (current-buffer)))
  query)

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

;;;###autoload
(eval-after-load 'clojure-mode
  '(progn
     (define-key clojure-mode-map (kbd "C-c M-=") 'nrepl-profile-toggle)
     (define-key clojure-mode-map (kbd "C-c M-_") 'nrepl-profile-clear)
     (define-key clojure-mode-map (kbd "C-c M--") 'nrepl-profile-summary)))


(provide 'nrepl-profile)

;;; nrepl-profile.el ends here
