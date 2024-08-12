'use client';

import { Checkbox, CheckboxGroup } from '@nextui-org/react';
import { useSession } from 'next-auth/react';
import React, { useEffect, useState } from 'react';
import { FaRandom } from 'react-icons/fa';

import GameCard from '@/app/components/GameCard';
import FilterBar from '@/app/components/inputs/FilterBar';
import SortByInput from '@/app/components/inputs/SortByInput';
import AddUserGameModal from '@/app/components/modals/AddUserGameModal';
import SteamImportModal from '@/app/components/modals/SteamImportModal';
import UserGameCard from '@/app/components/user/UserGameCard';

export default function TagSection({ games, selectedTags, setSelectedTags }) {
  const [tags, setTags] = useState([]);
  const staticTags = ['Multiplayer', 'Co-op', 'Local Multiplayer'];
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
    <div className="h-full min-w-[200px] bg-gray-800/30 shadow-inner flex flex-col p-3 gap-5">
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
  );
}
