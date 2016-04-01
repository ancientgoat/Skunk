"# Skunk" 

MyBranch
MyLogic

1 GT 4
OR
1 LT 14

```
{
    "decision" : {
        "name": "test01",
        [
            { "type" : "logic", "description":"SCr", "op": "GT", "value": 1.33 },
            { "type" : "logic", "op": "OR" },
            { "type" : "logic", "description":"X", "op": "LT", "value": 10 }
        ]
    }
}
```

```
input and output:
  {
    "animals":
    [
      {"type":"dog","name":"Spike","breed":"mutt",
          "leash_color":"red"},
      {"type":"cat","name":"Fluffy",
          "favorite_toy":"spider ring"}
    ]
  }
```
