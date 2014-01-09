import sys
import operator
import steamapi
import sqlite3

class Player:
  def __init__(self, player_id):
    self.player_id = player_id
    self.account = steamapi.user.SteamUser(self.player_id)
    self.friends = []
    self.games = []
    self.recent_games = []
    
  def dowload_datas(self):
    try:
      self.name = self.account.name
      games_raw = self.account.games
      recent_games = self.account.recently_played
      for app in games_raw:
        self.games.append([app.appid,
                           app.__str__().encode('ascii', 'ignore').replace("'", ""),
                           app.playtime_forever])
      for app in recent_games:
        self.recent_games.append(app.__str__().encode('ascii', 'ignore')) 
      return False
    except:
      print "There was an error trying to get Steam's datas (Is your id correct ?)"
      return True

  def dowload_friends(self):
    friends_raw = self.account.friends
    for friend in friends_raw:
      self.friends.append(friend.steamid)


class Database:
  def __init__(self, name):
    self.db = sqlite3.connect(name)
    
  def __del__(self):
    self.db.close()
  
  def add_player(self, player):
    c = self.db.cursor()
    for game in player.games:
      # Processing user's games
      c.execute("SELECT * FROM users WHERE id_user=" + str(player.player_id) +
                                     " AND id_game=" + str(game[0]))
      if c.fetchone() == None:
        c.execute("INSERT INTO users VALUES (" + str(player.player_id) + ", " + 
                  str(game[0]) + ", " + str(game[2]) + ")")
      else:
        c.execute("DELETE FROM users WHERE id_user=" + str(player.player_id) +
                                     " AND id_game=" + str(game[0]))
        c.execute("INSERT INTO users VALUES (" + str(player.player_id) + ", " + 
                  str(game[0]) + ", " + str(game[2]) + ")")
      # Processins games
      c.execute("SELECT * FROM games WHERE id_game=" + str(game[0]))
      if c.fetchone() == None:
        c.execute("INSERT INTO games VALUES (" + str(game[0]) + ", '" + game[1] + "')")
    self.db.commit()
  
  def add_players_friends(self, player):
    c = self.db.cursor()
    for friend_id in player.friends:
      friend = Player(friend_id)
      friend.dowload_datas()
      self.add_player(friend)


class Suggestor:
  def __init__(self, player, database):
    self.player = player
    self.database = database
    
  def suggest(self):
    self.suggested_games = dict()
    c = self.database.db.cursor()
    # Retrieving player top ten games
    player_games = []
    c.execute("SELECT id_game, playtime FROM users WHERE id_user=" + str(self.player.player_id))
    player_games_raw = c.fetchall()
    for game in player_games_raw:
      player_games.append(game[0])
    # Retrieving other users games
    c.execute("SELECT DISTINCT id_user FROM users")
    user_ids = c.fetchall()
    for user in user_ids:
      c.execute("SELECT id_game, playtime FROM users WHERE id_user=" +
                str(user[0]) + " ORDER BY playtime DESC LIMIT 10")
      user_games_raw = c.fetchall()
      user_games = []
      for games in user_games_raw:
          user_games.append(games[0])
      
      game_counter = 0
      for game in user_games:
        try:
          player_games.index(game)
          game_counter += 1
        except:
          game_counter = game_counter
      if game_counter >= 4:
        for game in user_games:
          if not game in player_games:
            try:
              self.suggested_games[game] += 1
            except:
              self.suggested_games[game] = 1
    suggested_games_e = dict(sorted(self.suggested_games.iteritems(),
                             key = operator.itemgetter(1), reverse = True)[:3])
    print "\033[1m\nWe suggest you to play:"
    for game in suggested_games_e:
      c.execute("SELECT name FROM games WHERE id_game=" + str(game))
      print "   - " + c.fetchone()[0]


def main(argv = None):
  print "\033[1mSTEAM RECOMMENDATION PROTOTYPE (Prototype X - Python) v1.0"
  print "----------------------------------------------------------\033[0m"
  steamapi.core.APIConnection("3365F0BE987ABFBDA9635BBD58058C99")
  
  myID = raw_input("What is your Steam id (number) ?\n>")
  player = Player(myID)
  quit = player.dowload_datas()
  
  if not quit:
    print "\033[1m-->" + player.name +" profile loaded\033[0m"
    database = Database('../fumistes.db')
    suggestor = Suggestor(player, database)

  while not quit:
    print "\033[0m\n----------------------------------------------------------"
    print "What do you want to do ?"
    print "Choices : - See my games (enter: games)"
    print "          - See my recently played games (enter: recently)"
    print "          - Recommend me a game (enter: recommend or suggest)"
    print "          - Quit programm (enter: quit)"
    choice = raw_input(">")
    if (choice == "games"):
      print player.games
    elif (choice == "recently"):
      print player.recent_games
    elif (choice == "quit"):
      quit = True
    elif (choice == "recommend" or choice == "suggest"):
      suggestor.suggest()
    elif (choice == "save"):
      database.add_player(player)
    elif (choice == "savefriends"):
      player.dowload_friends()
      database.add_players_friends(player)
    else:
      print "No valid entry"

if __name__ == "__main__":
  main()

