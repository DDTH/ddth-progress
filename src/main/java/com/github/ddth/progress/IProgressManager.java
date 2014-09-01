package com.github.ddth.progress;

/**
 * Progress-tracking record manager.
 * 
 * @author Thanh Nguyen <btnguyen2k@gmail.com>
 * @since 0.1.0
 */
public interface IProgressManager {
    /**
     * Creates a new progress-tracking record.
     * 
     * @return
     */
    public ProgressRecord create();

    /**
     * Updates an existing progress-tracking record.
     * 
     * @param record
     * @return
     */
    public ProgressRecord update(ProgressRecord record);

    /**
     * Gets an existing progress-tracking record.
     * 
     * @param id
     * @return
     */
    public ProgressRecord get(String id);

    /**
     * Removes an existing progress-tracking record.
     * 
     * @param id
     */
    public void remove(String id);
}
