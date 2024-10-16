'use client';

import { Checkbox, CheckboxGroup } from '@nextui-org/react';
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

export default function TagSection({ games, selectedTags, setSelectedTags }) {
  const [isOpen, setIsOpen] = useState(false);
  const [isLgScreen, setIsLgScreen] = useState(false);
  const [tags, setTags] = useState([]);
  const staticTags = ['Multiplayer', 'Co-op', 'Local Multiplayer'];

  const toggleMenu = () => setIsOpen(!isOpen);

  const handleTagChange = (values) => {
    setSelectedTags(values);
  };

  // Checks if window is lg
  useEffect(() => {
    // Check screen width on mount
    const handleResize = () => {
      setIsLgScreen(window.innerWidth >= 1024);
    };
    handleResize();
    window.addEventListener('resize', handleResize);
    return () => window.removeEventListener('resize', handleResize);
  }, []);

  const filterTags = () => {
    const tagCounts = games
      .map(({ tags }) => tags.map((tag) => tag.trim()))
      .flat()
      .reduce((counts, tag) => {
        counts[tag] = (counts[tag] || 0) + 1;
        return counts;
      }, {});

    const sortedTags = Object.entries(tagCounts)
      .sort(([, countA], [, countB]) => countB - countA)
      .slice(0, 20)
      .map(([tag]) => tag)
      .sort((a, b) => a.localeCompare(b));

    setTags(sortedTags.filter((tag) => !staticTags.includes(tag)));
  };

  useEffect(() => {
    filterTags();
  }, [games]);

  return (
    <div className={`h-full min-w-[200px] bg-gray-800/30 shadow-inner flex flex-col p-3 gap-5 relative´ ${isOpen || isLgScreen ? 'max-h-[100%]' : 'max-h-12'}`}>
      <button onClick={() => setIsOpen(!isOpen)} className="uppercase text-center text-lg justify-center flex flex-row gap-3 items-center lg:hidden">
        Tags
        {isOpen ? <IoIosArrowDropup /> : <IoIosArrowDropdown />}
      </button>

      <div className={`h-full w-full flex flex-col gap-5 ´ ${isOpen || isLgScreen ? 'overflow-auto' : 'overflow-hidden'}`}>
        <CheckboxGroup label="Multiplayer Tags" values={selectedTags} onChange={handleTagChange}>
          {staticTags?.map((tag) => (
            <Checkbox key={tag} value={tag}>
              {tag}
            </Checkbox>
          ))}
        </CheckboxGroup>

        <CheckboxGroup label="Tags Filters" values={selectedTags} onChange={handleTagChange}>
          {tags?.map((tag) => (
            <Checkbox key={tag} value={tag}>
              {tag}
            </Checkbox>
          ))}
        </CheckboxGroup>
      </div>
    </div>
  );
}
