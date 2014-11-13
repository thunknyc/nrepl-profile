# nrepl-profile

nREPL support for [thunknyc/profile](http://github.com/thunknyc/profile).

## Installation

Add `[thunknyc/nrepl-profile "0.1.0-SNAPSHOT"]` as a plugin in your `:user` profile inside `${HOME}/.lein/profiles.clj`.

Stick `cider-profile.el` somewhere emacs can see it and then `(require 'cider-profile)` inside your `${HOME}/.emacs`, `${HOME}/.emacs.d/init.el`, whatever.

## Keybindings

* `C-c M-=` toggles profiling status.
* `C-c M--` displays profiling data to `*err*`.
* `C-c M-_` clears collected profiling data.

## TODO

* Write a README.
* Package the elisp and make it accessible from MELPA.

## License

Copyright © 2014 Edwin Watkeys

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
