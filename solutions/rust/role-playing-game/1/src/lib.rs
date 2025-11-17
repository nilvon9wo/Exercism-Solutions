pub struct Player {
    pub health: u32,
    pub mana: Option<u32>,
    pub level: u32,
}

impl Player {
    pub fn revive(&self) -> Option<Player> {
        match self.health {
            health if health > 0 => None,
            _ => match self.level {
                level if level >= 10 => Some(Player {
                    health: 100,
                    mana: Some(100),
                    ..*self
                }),
                _ => Some(Player{
                    health: 100,
                    mana: None,
                    ..*self
                })
            }
        }
    }

    pub fn cast_spell(&mut self, mana_cost: u32) -> u32 {
        match self.mana {
            None => {
                self.health -= mana_cost;
                0
            },
            Some(mana) if mana < mana_cost => {
                0
            },
            Some(mana) if mana == mana_cost => {
                self.mana = None;
                mana_cost * 2
            }
            Some(mana) => {
                self.mana = Some(mana - mana_cost);
                mana_cost * 2
            }
        }
    }
}
