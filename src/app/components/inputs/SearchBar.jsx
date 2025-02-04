'use client';

import { Autocomplete, AutocompleteItem } from '@nextui-org/react';
import { useAsyncList } from '@react-stately/data';
import { useSession } from 'next-auth/react';

import { GETSIGNAL } from '@/app/api/signalRequest';
import { GET } from '@/app/api/tokenRequest';

export default function SearchBar({ setGame, setGrids }) {
  // Get Session
  const { data: session } = useSession();

  const getGrids = async (key) => {
    const formURL = `api/sgdb/getGrids/${key}`;
    let res = await GET(formURL, session.user.token);
    setGrids(res.data.filter((item) => item.width === 600 && item.height === 900).map((item) => item.url));
  };

  let list = useAsyncList({
    async load({ signal, filterText }) {
      try {
        if (filterText) {
          let res = await GETSIGNAL('api/sgdb/search?term=' + filterText, session.user.token, signal);
          return {
            items: res.data,
          };
        } else {
          return {
            items: [],
          };
        }
      } catch (error) {
        console.error('Error:', error);
      }
    },
  });

  const handleSelection = async (key) => {
    if (!key) {
      return;
    }

    // Find the selected item based on the key
    const selectedItem = list.items.find((item) => item.id === key);

    // If the selected item is found, set the game with its name
    if (selectedItem) {
      setGame({ key: selectedItem.id, name: selectedItem.name });
      await getGrids(selectedItem.id);
    }
  };

  return (
    <Autocomplete
      placeholder="Buscar juego"
      aria-label="search"
      className="w-full"
      allowsCustomValue
      inputValue={list.filterText}
      isLoading={list.isLoading}
      defaultItems={list.items}
      onInputChange={list.setFilterText}
      onSelectionChange={(key) => handleSelection(key)}>
      {(item) => (
        <AutocompleteItem key={item.key} className="capitalize">
          {item.name}
        </AutocompleteItem>
      )}
    </Autocomplete>
  );
}
