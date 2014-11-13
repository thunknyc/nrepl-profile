# nrepl-profile

nREPL and CIDER support for
[thunknyc/profile](http://github.com/thunknyc/profile).

## Installation

Add `[thunknyc/nrepl-profile "0.1.0-SNAPSHOT"]` to the vector
associated with the `:plugins` key of your `:user` profile inside
`${HOME}/.lein/profiles.clj`. Schematically, like this:

```clojure
{:user {:plugins [[thunknyc/nrepl-profile "0.1.0-SNAPSHOT"]]}}
```

Obviously the plug-in for CIDER needs to be in there too.

For Emacs, stick `cider-profile.el` somewhere accessible and add
`(require 'cider-profile)` to your `${HOME}/.emacs`,
`${HOME}/.emacs.d/init.el`, whatever.

## Keybindings

* `C-c M-=` toggles profiling status.
* `C-c M--` displays profiling data to `*err*`.
* `C-c M-_` clears collected profiling data.

## TODO

* Write a better README.
* Package the elisp and make it accessible from MELPA.

## License

Copyright © 2014 Edwin Watkeys

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
