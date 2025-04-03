# Hexchanting

Imbue your equipment with the power of Hex
Casting. [Hex Book is here](https://arconyx.github.io/hexchanting/v/latest/main/en_us/). For Fabric on Minecraft 1.20.1.

## Features

- Craft amethyst axes, hoes, pickaxes and shovels that cast after breaking a block, pushing the location of the block to
  the stack. Enchantments propagate when the Break Block spell is cast using this tool.
- Wield an amethyst sword that casts on hit, pushing the targeted entity to the stack.
- Wear amethyst armour that casts on various triggers. Helmets cast on aggro, chestplate when damaged, leggings on death
  and boots when falling. The stack is initialised with relevant iotas.
- Fire amethyst arrows, with an embedded hex and a small media supply. Casts on hitting an entity or a block, pushing
  itself and the unfortunate target (or null) to the stack. ~~Are these discount projectile wisps? Yes.~~

## Futures

- Shields, if I can be bothered dealing with the all the hardcoded logic involved in the vanilla shield implementation.
- No Forge port, unless I start playing on Forge.
- Return identifiers when breaking blocks (wait on scrying being broken out of hexical)
- Apply arrow mishaps to the target? Maybe.
- Prohibit mending?
- Cast on mine for jeweller's hammer?
- Disable or reduce particles
- ???

## Notes

- Cast on mine doesn't work in creative because it bypasses the tool entirely.
- This is mostly made for personal use. Updates will be sporadic.
- If you want to know what you can do with this, go read the license. I encourage you to tweak it, port it, borrow parts
  or whatever else you want to do and share the results with the world.
- I am indebted to miyucomics and TechTastic, not that they know it, since their
  projects [Hexcellular](https://github.com/miyucomics/hexcellular/) and [HexWeb](https://github.com/TechTastic/HexWeb)
  were valuable reference points when I was getting started.
- Also to everyone involved in making [HexDoc](https://github.com/hexdoc-dev/hexdoc) because they made documentation
  easy.
- And to everyone who has offered feedback in the Hex Casting Discord.