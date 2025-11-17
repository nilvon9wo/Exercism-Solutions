module AnnalynsInfiltration

let canFastAttack (knightIsAwake: bool): bool
    = not(knightIsAwake)

let canSpy (knightIsAwake: bool) (archerIsAwake: bool) (prisonerIsAwake: bool): bool
    = knightIsAwake || archerIsAwake || prisonerIsAwake

let canSignalPrisoner (archerIsAwake: bool) (prisonerIsAwake: bool): bool
    = prisonerIsAwake
      && not(archerIsAwake)  

let canDogScareKnight (archerIsAwake: bool) (petDogIsPresent: bool)
    = (petDogIsPresent && not(archerIsAwake))
    
let canAnnalynsBeSneakyEnough (knightIsAwake: bool) (archerIsAwake: bool) (prisonerIsAwake: bool) (petDogIsPresent: bool)
    = not(petDogIsPresent) && prisonerIsAwake && not(knightIsAwake || archerIsAwake)
    
let canFreePrisoner (knightIsAwake: bool) (archerIsAwake: bool) (prisonerIsAwake: bool) (petDogIsPresent: bool): bool
    = (canDogScareKnight archerIsAwake petDogIsPresent)
        || (canAnnalynsBeSneakyEnough knightIsAwake archerIsAwake prisonerIsAwake petDogIsPresent)
       
