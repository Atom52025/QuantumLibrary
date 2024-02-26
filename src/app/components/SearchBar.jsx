'use client';

import { Autocomplete, AutocompleteItem } from '@nextui-org/react';
import { useAsyncList } from '@react-stately/data';
import { useSession } from 'next-auth/react';

import { GET } from '@/app/api/signalRequest';

export default function SearchBar({ setGame, setGrids}) {
  // Get Session
  const { data: session } = useSession();

  const getGrids = async (key) => {
    const formURL = `api/sgdb/getGrids/${key}`;
    let res = await GET(formURL, session.user.token);
    console.log(res.data)
    setGrids(res.data
        .filter(item => item.width === 600 && item.height === 900)
        .map(item => item.url));
  };

  let list = useAsyncList({
    async load({ signal, filterText }) {
      try {
        let res = await GET('api/sgdb/search?term=' + filterText, session.user.token, signal);
        return {
          items: res.data,
        };
      } catch (error) {
        console.error('Error:', error);
      }
    },
  });

  const handleSelection = async (key) => {
    setGame({ key: key, name: list.filterText});
    await getGrids(key);
  };

  return (
    <Autocomplete
        placeholder="Search a game"
        aria-label="search"
        className="w-full"
        inputValue={list.filterText}
        isLoading={list.isLoading}
        items={list.items}
        onInputChange={list.setFilterText}
        onSelectionChange={(key) => handleSelection(key)}
        >
      {(item) => (
        <AutocompleteItem key={item.key} className="capitalize">
          {item.name}
        </AutocompleteItem>
      )}
    </Autocomplete>
  );
}
