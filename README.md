# nrepl-profile

nREPL and CIDER support for
[thunknyc/profile](http://github.com/thunknyc/profile).

## Introduction

Profiling is a rich and varied field of human endeavour. I encourage
you to consider what you're trying to accomplish by profiling. This
package for CIDER may not be suited to your current needs. What is
nrepl-profile good for? It's intended for interactive, coarse-grained
profiling applications where JVM warm-up and garbage collection are
not concerns. If you are doing numeric computing or writing other
processor-intensive code, I recommend you check out
[Criterium](https://github.com/hugoduncan/criterium).

On the other hand, if you are primarily concerned about the influence
of JVM-exogenous factors on your code—HTTP requests, SQL queries,
other network- or (possibly) filesystem-accessing operations—then this
package may be just what the doctor ordered.

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
