import sys
import steamapi

class Player:
  def __init__(self, player_id):
    self.player_id = player_id
    self.account = steamapi.user.SteamUser(self.player_id)
    self.games = []
    self.recent_games = []
    
  def dowload_datas(self):
    try:
      games_raw = self.account.games
      recent_games = self.account.recently_played
      for app in games_raw:
        self.games.append(app.__str__().encode('ascii', 'ignore'))
      for app in recent_games:
        self.recent_games.append(app.__str__().encode('ascii', 'ignore')) 
      return False
    except:
      print "There was an error trying to get Steam's datas"
      return True

def main(argv = None):
  print "\033[1mSTEAM RECOMMENDATION PROTOTYPE (Prototype X - Python) v1.0"
  print "\033[0m----------------------------------------------------------"
  steamapi.core.APIConnection("3365F0BE987ABFBDA9635BBD58058C99")

  myID = raw_input("What is your Steam id (number) ?\n>")
  player = Player(myID)
  quit = player.dowload_datas()
  print quit

  while not quit:
    print "\nWhat do you want to do ?"
    print "Choices : - See my games (enter: games)"
    print "          - See my recently played games (enter: recently)"
    print "          - Quit programm (enter: quit)"
    choice = raw_input(">")
    if (choice == "games"):
      print player.games
    elif (choice == "recently"):
      print player.recent_games
    elif (choice == "quit"):
      quit = True
    else:
      print "No value entry"

if __name__ == "__main__":
  main()

