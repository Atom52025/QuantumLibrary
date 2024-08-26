import { Input } from '@nextui-org/react';

export default function AchivementsInput({ achievements, setAchievements, totalAchievements, setTotalAchievements }) {
  const handleAchievementsChange = (value) => {
    const newValue = Math.min(value, totalAchievements);
    setAchievements(newValue);
  };

  const handleTotalAchievementsChange = (value) => {
    setTotalAchievements(value);
    if (achievements > value) {
      setAchievements(value);
    }
  };

  return (
    <Input
      label="Logros"
      value={achievements ? achievements : ''}
      onValueChange={handleAchievementsChange}
      type="number"
      endContent={
        <div className={'flex-row flex gap-2 items-center'}>
          <p>{' / '}</p>
          <Input
            label="Logros totales"
            classNames={{
              inputWrapper: '!border-0 h-2 p-0',
            }}
            value={totalAchievements ? totalAchievements : ''}
            onValueChange={handleTotalAchievementsChange}
            type="number"
            variant="bordered"
            required={true}
          />
        </div>
      }
    />
  );
}
