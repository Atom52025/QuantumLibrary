'use client';

import { Checkbox, CheckboxGroup, ScrollShadow } from '@nextui-org/react';
import { useSession } from 'next-auth/react';
import React, { useEffect, useState } from 'react';
import { FaRandom } from 'react-icons/fa';
import { IoIosArrowDropdown, IoIosArrowDropup } from 'react-icons/io';

import FilterBar from '@/app/components/inputs/FilterBar';
import SortByInput from '@/app/components/inputs/SortByInput';
import AddUserGameModal from '@/app/components/modals/AddUserGameModal';
import SteamImportModal from '@/app/components/modals/SteamImportModal';
import GameCard from '@/app/components/non-user/GameCard';
import UserGameCard from '@/app/components/user/UserGameCard';
import { Button } from '@nextui-org/button';

export default function TagSection({ games, selectedTags, setSelectedTags }) {
  const [isOpen, setIsOpen] = useState(false);
  const [isLgScreen, setIsLgScreen] = useState(false);
  const [tags, setTags] = useState([]);
  const staticTags = ['Multiplayer', 'Co-op', 'Local Multiplayer'];

  const toggleMenu = () => setIsOpen(!isOpen);

  const handleTagChange = (values) => {
    setSelectedTags(values);
  };

  const filterTags = () => {
    const tagCounts = games
      .map(({ tags }) => tags.map((tag) => tag.trim()))
      .flat()
      .reduce((counts, tag) => {
        counts[tag] = (counts[tag] || 0) + 1;
        return counts;
      }, {});

    // Sort tags based on frequency from highest to lowest
    const sortedTags = Object.entries(tagCounts)
      .sort(([, countA], [, countB]) => countB - countA)
      .slice(0, 30)
      .map(([tag]) => tag);

    // Filter out static tags
    setTags(sortedTags.filter((tag) => !staticTags.includes(tag)));
  };

  // Checks if window is md
  useEffect(() => {
    // Check screen width on mount
    const handleResize = () => {
      setIsLgScreen(window.innerWidth >= 768);
    };
    handleResize();
    window.addEventListener('resize', handleResize);
    return () => window.removeEventListener('resize', handleResize);
  }, []);

  useEffect(() => {
    filterTags();
  }, [games]);

  return (
    <div className={`md:min-h-full md:min-w-[220px] md:w-auto w-full bg-gray-800/30 shadow-inner flex flex-col p-3 gap-5 relative´ ${isOpen || isLgScreen ? 'max-h-[100%]' : 'max-h-12'}`}>
      <button onClick={() => setIsOpen(!isOpen)} className="uppercase text-center text-md justify-center flex flex-row gap-3 items-center md:hidden">
        Tags
        {isOpen ? <IoIosArrowDropup /> : <IoIosArrowDropdown />}
      </button>
      <ScrollShadow className={`h-full w-full flex flex-col gap-5 overflow-x-hidden`}>
        <CheckboxGroup values={selectedTags} onChange={handleTagChange}>
          <p className="relative text-medium text-foreground-500">Multiplayer Tags</p>
          {staticTags?.map((tag) => (
            <Checkbox key={tag} value={tag}>
              {tag}
            </Checkbox>
          ))}
          <p className="relative text-medium text-foreground-500">Tags</p>
          {tags?.map((tag) => (
            <Checkbox key={tag} value={tag}>
              {tag}
            </Checkbox>
          ))}
        </CheckboxGroup>
      </ScrollShadow>
    </div>
  );
}
