 - [X] pull in parse-opts;

 - [X] handle the case where either the width or the height is 1 (to avoid
       divide-by-zero errors);

 - [ ] I'm a bit sloppy with types at the moment, switching between double and
       rational without much thought - this should get normalized and nailed
       down better before any of this is considered stable;
