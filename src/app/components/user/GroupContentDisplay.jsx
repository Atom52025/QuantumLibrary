'use client';

import { Avatar, AvatarGroup, ScrollShadow } from '@nextui-org/react';
import { useSession } from 'next-auth/react';
import React, { useEffect, useState } from 'react';

import { PATCH } from '@/app/api/tokenRequest';
import Counter from '@/app/components/Counter';
import FilterBar from '@/app/components/inputs/FilterBar';
import SortByInput from '@/app/components/inputs/SortByInput';
import GameCard from '@/app/components/non-user/GameCard';
import GroupListSection from '@/app/components/sections/GroupListSection';
import TagSection from '@/app/components/sections/TagSection';

export default function GroupContentDisplay({ data, group, gData }) {
  // Get Session
  const { data: session } = useSession({ required: true });

  const [games, setGames] = useState(data);
  const [filteredGames, setFilteredGames] = useState(games.sort((a, b) => a.name.localeCompare(b.name)));
  const [searchParam, setSearchParam] = useState('');
  const [random, setRandom] = useState(-1);
  const [open, setOpen] = useState(false);
  const [selectedTags, setSelectedTags] = useState([]);
  const [order, setOrder] = useState('nameDown');

  // Function to order the games
  useEffect(() => {
    orderGames();
  }, [order]);

  const orderGames = () => {
    switch (order) {
      case 'nameDown':
        setFilteredGames((prevGames) => [...prevGames].sort((a, b) => a.name.localeCompare(b.name)));
        break;
      case 'nameUp':
        setFilteredGames((prevGames) => [...prevGames].sort((a, b) => b.name.localeCompare(a.name)));
        break;
      case 'mostVoted': // New case for ordering by most votes
        setFilteredGames((prevGames) => [...prevGames].sort((a, b) => b.votes.length - a.votes.length));
        break;
      case 'leastVoted': // Option for least voted to most voted
        setFilteredGames((prevGames) => [...prevGames].sort((a, b) => a.votes.length - b.votes.length));
        break;
    }
  };

  // Function to filter the games
  const filterGames = () => {
    if (selectedTags.length === 0) {
      setFilteredGames(games.filter((game) => game.name.toLowerCase().includes(searchParam.toLowerCase())));
      orderGames();
    } else {
      setFilteredGames(
        games.filter((game) => selectedTags.every((tag) => game.tags.map((tag) => tag.trim()).includes(tag))).filter((game) => game.name.toLowerCase().includes(searchParam.toLowerCase())),
      );
      orderGames();
    }
  };

  // Function to get a random game
  const randomGame = () => {
    // Step 1: Find the maximum number of votes
    const maxVotes = Math.max(...filteredGames.map((game) => game.votes.length));

    // Step 2: Filter the games that have the maximum number of votes
    const mostVotedGames = filteredGames.filter((game) => game.votes.length === maxVotes);

    // Step 3: Pick a random game from the filtered list
    const randomIndex = Math.floor(Math.random() * mostVotedGames.length);
    setRandom(mostVotedGames[randomIndex]?.id); // Safely set the random game's id
  };

  // Function to vote for a game
  const voteGame = async (gameId) => {
    const formURL = `api/user/${session.user.username}/groups/${group.id}/game/${gameId}`;

    try {
      await PATCH(formURL, session.user.token);

      // Update the games list
      setGames((prevGames) =>
        prevGames.map((game) => {
          if (game.id === gameId) {
            const hasVoted = game.votes.includes(session.user.username);
            return {
              ...game,
              votes: hasVoted
                ? game.votes.filter((username) => username !== session.user.username) // Remove vote
                : [...game.votes, session.user.username], // Add vote
            };
          }
          return game;
        }),
      );
    } catch (error) {
      console.error('Error al votar por el juego');
    }
  };

  useEffect(() => {
    filterGames();
  }, [games, selectedTags, searchParam]);

  return (
    <>
      <TagSection games={games} setSelectedTags={setSelectedTags} selectedTags={selectedTags} />
      <div className={`max-h-full w-full ${open ? 'mr-[200px]' : 'mr-0'}`}>
        <div className="w-full h-full flex flex-col relative">
          <div className="md:px-10 px-5 py-5 flex flex-wrap gap-2 md:justify-between justify-center sticky top-0 bg-gray-900">
            <FilterBar searchParam={searchParam} setSearchParam={setSearchParam} />
            <SortByInput setOrder={setOrder} groupgame />
          </div>
          <ScrollShadow className="h-full w-full shadow-inner overflow-y-scroll">
            <div className="md:px-10 px-5 py-5 grid lg:grid-cols-6 sm:grid-cols-5 grid-cols-3 gap-3">
              {filteredGames?.map((entry) => (
                <div
                  key={entry.id}
                  className={`aspect-[6/9] bg-gray-600 rounded-xl overflow-hidden relative ${entry.votes.length > 0 ? 'border-5 border-blue-600' : ''}`}
                  onClick={() => voteGame(entry.id)}>
                  <GameCard entry={entry} setGames={setGames} random={random} noModal={true} />
                  <div className="absolute top-1 right-1 p-1 rounded-bl-xl z-30">
                    <AvatarGroup isBordered>
                      {entry.votes.map((user, index) => {
                        const matchedUser = group.userGroups.find((groupUser) => groupUser.user.username === user);
                        return <Avatar key={index} src={matchedUser ? matchedUser.user.image : ''} />;
                      })}
                    </AvatarGroup>
                  </div>
                </div>
              ))}
            </div>
          </ScrollShadow>
        </div>
      </div>
      <GroupListSection gData={gData} open={open} setOpen={setOpen} />
      <Counter randomGame={randomGame} filteredGames={filteredGames} open={open} />
    </>
  );
}
