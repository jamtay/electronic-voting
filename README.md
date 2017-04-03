# Mac setup
## Prerequisties 
Enter the follwoing commands to the terminal to insatll homebrew and then sbt
1. `/usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"`
2. `brew install sbt`

## Instructions
1. Clone repository `git clone https://github.com/jamtay/electronic-voting.git`
2. Enter `cd electronic voting` in the terminal
2. From the terminal enter `sbt clean run`
3. Wait for `"(Server started, use Ctrl+D to stop and go back to the console...)"` to apperar in the terminal window
4. In your browser navigate to [localhost:9000](http://localhost:9000)

# Windows setup

## Prerequisties 
Enter the follwoing commands to the terminal to insatll homebrew and then sbt
1. `/usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"`
2. `brew install sbt`

## Instructions
1. Clone repository `git clone https://github.com/jamtay/electronic-voting.git`
2. Enter `cd electronic voting` in the terminal
2. From the terminal enter `sbt clean run`
3. Wait for `"(Server started, use Ctrl+D to stop and go back to the console...)"` to apperar in the terminal window
4. In your browser navigate to [localhost:9000](http://localhost:9000)


# How to start an election
1. Login as an admin
2. Click start election and logout
3. Login as a user
4. Register and vote
5. When you want to end the election, login as the admin again and click end election.  This will display the final election results

See `Global.Java` for a list of login credentials