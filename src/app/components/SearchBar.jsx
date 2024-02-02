'use client';

import { Autocomplete, AutocompleteItem } from '@nextui-org/react';
import { useAsyncList } from '@react-stately/data';
import { useSession } from 'next-auth/react';

import { GET } from '@/app/api/signalRequest';

export default function SearchBar() {
  // Get Session
  const { data: session, status } = useSession();

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

  return (
    <Autocomplete inputValue={list.filterText} isLoading={list.isLoading} items={list.items} placeholder="Search a game" className="max-w-xs" onInputChange={list.setFilterText} aria-label="search">
      {(item) => (
        <AutocompleteItem key={item.id} className="capitalize">
          {item.name}
        </AutocompleteItem>
      )}
    </Autocomplete>
  );
}
