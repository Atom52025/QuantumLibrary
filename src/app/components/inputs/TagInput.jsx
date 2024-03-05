import { Button, Chip, Input } from '@nextui-org/react';
import { useState } from 'react';
import { PiArrowBendDownRightBold } from 'react-icons/pi';

export default function TagInput({ tags, setTags }) {
  // Tag input
  const [tagInput, setTagInput] = useState('');

  const addTag = () => {
    if (tags.find((tag) => tag === tagInput)) return;
    setTags([...tags, tagInput]);
    setTagInput('');
  };

  const removeTag = (tagToRemove) => {
    setTags(tags.filter((tag) => tag !== tagToRemove));
  };

  return (
    <>
      <Input
        label="Tags"
        placeholder="Introduzca un tag"
        type="text"
        variant="bordered"
        required
        value={tagInput}
        onValueChange={setTagInput}
        endContent={
          <Button isIconOnly onClick={addTag} className="h-full">
            <PiArrowBendDownRightBold />
          </Button>
        }
        onKeyDown={(e) => {
          if (e.key === 'Enter') {
            addTag();
            setTagInput('');
          }
        }}
      />
      <div className={'flex flex-row gap-4'}>
        {tags.map((tag) => (
          <Chip onClose={() => removeTag(tag)} key={tag}>
            {tag}
          </Chip>
        ))}
      </div>
    </>
  );
}
