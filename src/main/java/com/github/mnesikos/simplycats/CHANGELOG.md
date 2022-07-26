# Changelog
All notable changes to this project will be documented in this file.

The format is based on 
[Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to 
[Forge Versioning Conventions](https://mcforge.readthedocs.io/en/latest/conventions/versioning/).

## [1.12.2-0.2.0] - 2022/07/19
### Added
- Color inhibitor gene; adds silver tabbies and smokes!
- Blue-eyed and red-eyed albino alleles; true albino cats!

## [1.12.2-0.0.4.1] - 2021/08/25
### Fixed
- Cats disappearing from existing worlds due to invalid NBT data
- Kitten scaling effected by changing the config's kitten mature timer
- TODO Kitten tails scaling with body properly through aging
- Tree posts no longer missing the bottom face texture

## [1.12.2-0.0.4.0] - 2021/08/24
### Added
- Long hair cat model edits! All cats that were genetically long hair before will now use these cute new edits.
- New colors: double dilutions, adding 70+ new color combinations. The genetics for these existed before this update, now the textures will match their phenotype!
- Vanilla cat sitting on stuff AI! Yes, that really annoying feature... good luck.
- 2 Super Not-So-Secret name tag easter eggs for a couple specific people!
- Cat tree block pieces! A bed, a post, and a box, all in each of the dye colors.
- Window perches for the cats to sit on (like chests!), comes in all vanilla plank variants.
- Parrot support for the pet carrier and adopt certificates.
- Chinese localization thanks to hackwellfox on Github!
### Changed
#####**!!** ALL blocks have been changed, existing worlds WILL LOSE any cat blocks in them **!!**
#####**!!** Adopt AND release certificates have been changed, existing worlds WILL LOSE all certificates **!!**
- Cat hitbox height reduced slightly, and shadow is slightly larger; both now matching vanilla cats.
- Kittens grow gradually now, and older kittens will explore further from their parent!
- All chat messages from the mod are status messages now, no more chat spam finally.
- Litter boxes are no longer tile entities!!
- Radius of laser pointer is now 4 blocks, and based around where the laser is pointing instead of the player.
### Removed
- Beckon command... useless and not worth keeping around.
### Fixed
- Kittens will no longer follow an adult cat if they are sitting.
- Tamed cats will no longer stop sitting if their owner is attacked- no more accidental breedings lol whoops
- Blocks no longer connect to fences, glass panes, and the like.

## [1.12.2-0.0.3.1] - 2020/08/14
### Added
- New ticked tabby texture for homozygous ticked cats, slightly edited texture for residual-ticked cats as well.
- Bobtails!!! They're so cute and stumpy (and rare).
- Cat Sterilization Potion! No more snipping... with shears...
- Animania chickens and certain species' babies have been added to the prey list.
### Changed
- Cats now have 14 health! (New cats from 0.0.3.0 already saw this change, now pre-existing cats have been fixed.)
- Instead of using a stick to get information on your cat, give them a name and sneak while looking at them!
- Certificates are no longer limited to 1 item stack sizes! This was annoying.
### Fixed
- Removed extra space in the nametag recipe, now it's truly 2x2 craftable.
- Cat names in the book gui actually centered now (lol whoops).
- Prevents possible TE crashes now.
- Catnip seeds can be planted on modded farmland.

## [1.12.2-0.0.3.0] - 2020/07/19
### Added
- Creepers will now avoid SC cats exactly like their vanilla counterparts.
- Witches are suddenly pacifists with SC cats around!
- Cat book! Keep track of all your cats and their info.
- Catnip crop, acquire from shelter workers.
- A laser pointer!! Cute and fun. Written by SoggyMustache.
- New blocks: food bowl, litter box, and scratching post (all currently decoration only).
- New attack AI for cats, with a sneak animation AND configurable prey list!
- A maximum limit of tamed cats per player now able to be set via config, disabled by default.
- Your tamed cats are now kept track of, including a command to edit the number if necessary. A join message can be enabledin the config!
- Cats have a couple new breeding rules, they will not breed if: their owner is offline, or their owner exceeds the tame limit (config).
- Cats whose owner is offline will not die.
### Changed
- Updated Forge Mappings
- Nametag recipe is now 1 ingot instead of two.
- Kittens are smaller for longer, yay! (This time is also configurable.)
- Removed maximum limit on wander area idk do what you want I guess lol.
- Updated treat bag! Right click to shake the bag and call all your nearby cats to your location.
### Fixed
- Cats can no longer mate through walls or even 1x1 gaps.
- Cats with home points will now wander correctly.
- Pet carriers containing cats will once again give you a description of the cat!

## [1.12.2-0.0.2.2] - 2019/09/28
### Added
- Kittens now get the home position of their mother.
### Changed
- Readjusted how white paws are chosen (all 4 white paws are now more common than just 1-3).
- Blaze powder now cuts timers in half on each use instead of a hardcoded set.
### Fixed
- Method change for how carriers save data. Fixes kitten issue.

## [1.12.2-0.0.2.1] - 2019/09/21
### Fixed
- Hotfix for older forge versions. This mod is built with Forge 2847 and is meant to run on that version.

## [1.12.2-0.0.2.0] - 2019/09/21
### Added
- Tons of new cat textures!!
- Treat Bag item! Shift + use this on a cat to set/unset their home point! Cats will also follow you... (Thank you for the texture, Fisk!!)
### Changed
- Updated forge version
- Entire genetics system has been reworked!
### Removed
- Cat kibble items (they're bad for your cats anyways)
### Fixed
- Good riddance breeding crash on newer versions of forge (thanks to SoggyMustache) 
- Village Shelter structure edited a tad


## [1.12.2-0.0.1.2] - 2019/07/29
### Added
- Breeding limit based on configurable number cats in 16 block area.
### Changed
- Version number.
- New cat food and catnip textures! Big thank you to fisk.
- Shelter cats now spawn neutered or spayed.
### Removed
- Removed purr timer logger.
- Unused litter item.
### Fixed
- Pregnant cats who are still in heat not giving birth. ("inheat" was never set to false, so BirthAI was never called.)
- Adoption and Release Certificates now get used up instead of having infinite uses.

## [1.12.2-0.0.1.1] - unreleased

## [1.12.2-0.0.1.0] - private release
### Added
- Cats! Cats can be spayed/neutered with shears (shift right click).
- Blaze Powder can be used as a "Heat Inducer" - speeds up the heat cooldown.
- Shift right clicking with a stick or bone on a cat can get you various information about them!
- Pet Carrier and cat food items (food does nothing yet lol).
- Pet Shelter structure and new villager.
- Added food items compatible with ore dictionary - only use atm is for healing.
- Chance of purring sounds with each interaction.
- Two certificate items for adoption and releasing, thank you so much to Captain for the textures!!

## [Unreleased] - YYYY/MM/DD
### Added
### Changed
### Deprecated
### Removed
### Fixed
### Security