module LuciansLusciousLasagna
let expectedMinutesInOven = 40
let remainingMinutesInOven(minutesAlreadyInOven: int): int =  expectedMinutesInOven - minutesAlreadyInOven

let minutesToPrepareEachLayer = 2
let preparationTimeInMinutes(layerCount: int): int = minutesToPrepareEachLayer * layerCount

let elapsedTimeInMinutes(layerCount: int)(minutesAlreadyInOven: int): int = preparationTimeInMinutes(layerCount) + minutesAlreadyInOven
