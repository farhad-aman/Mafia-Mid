package model.network;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import model.logic.*;

public class ChatRoom 
{

    private ArrayList<Player> players;

    private ArrayList<Player> alivePlayers;

    private ArrayList<Chat> chats;

    public ChatRoom()
    {
        players = new ArrayList<>();
        alivePlayers = new ArrayList<>();
        chats = new ArrayList<>();
    }

    public void readFrom(God god, Player player)
    {
        Thread thread = new Thread(new ReadingChatHandler(god, player));
        thread.start();
    }

    public void readFromAll(God god)
    {
        for(Player player : players)
        {
            readFrom(god, player);
        }
    }

    public void sendTo(Chat chat, Player dest) 
    {
        try 
        {
            dest.getObjectOutputStream().writeObject(chat);
        } 
        catch (Exception e) 
        {
            System.out.println("sending chat error.");
            e.printStackTrace();
        }
    }

    public void sendTo(Chat chat, ArrayList<Player> dest)
    {
        for(Player p : dest)
        {
            sendTo(chat, p);
        }
    }

    public void sendToAll(Chat chat)
    {
        sendTo(chat, players);
    }

    public void sendToAllAlive(Chat chat)
    {
        for(Player p : players)
        {
            if(p.getIsAlive() == true)
            {
                sendTo(chat, p);
            }
        }
    }

    public void connect(int port, int playersCount)
    {
        int clientCount = 0;
        try(ServerSocket server = new ServerSocket(port)) 
        {
            while(clientCount < playersCount)
            {
                System.out.println("Server Is Waiting For A New Client.");
                Socket channel = server.accept();
                System.out.println("A Socket Created.");
                Thread thread = new Thread(new ConnectionHandler(channel, this));
                thread.start();
                System.out.println("A Client Accepted.");
                clientCount++;
            }
            try 
            {
                Thread.sleep(3000);
            } 
            catch (InterruptedException e) 
            {
                e.printStackTrace();
            }
        } 
        catch(Exception e) 
        {
            e.printStackTrace();
        }
        while(this.players.size() < playersCount)
        {
            try 
            {
                Thread.sleep(500);
            } 
            catch (InterruptedException e) 
            {
                e.printStackTrace();
            }
        }
        System.out.println("All Clients Are Connected.");
    }

    public ArrayList<Player> getPlayers()
    {
        return players;
    }

    public synchronized boolean addPlayer(Player player)
    {
        for(Player p : players)
        {
            if(player.getUserName().trim().equals(p.getUserName().trim()))
            {
                return false;
            }
        }
        players.add(player);
        return true;
    }
}
