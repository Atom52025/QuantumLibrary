import { Input } from '@nextui-org/react';
import React from 'react';
import { RiSearch2Line } from 'react-icons/ri';

export default function FilterBar({ searchParam, setSearchParam }) {
  return (
    <Input
      isClearable
      variant={'underlined'}
      radius="lg"
      classNames={{
        base: 'max-w-md',
        inputWrapper: 'h-unit-10',
      }}
      value={searchParam}
      onValueChange={setSearchParam}
      placeholder="Buscar..."
      startContent={<RiSearch2Line className="text-black/50 mb-0.5 dark:text-white/90 text-slate-400 pointer-events-none flex-shrink-0" />}
    />
  );
}
